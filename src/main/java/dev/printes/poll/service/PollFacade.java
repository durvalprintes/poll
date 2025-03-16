package dev.printes.poll.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dev.printes.poll.client.ValidatorClient;
import dev.printes.poll.mapper.PollMapper;
import dev.printes.poll.model.dto.PollMessageDTO;
import dev.printes.poll.model.dto.PollRequestDTO;
import dev.printes.poll.model.dto.PollResultDTO;
import dev.printes.poll.model.entity.Associate;
import dev.printes.poll.model.entity.PollSession;
import dev.printes.poll.model.entity.Voting;
import dev.printes.poll.model.enums.ResultEnum;
import dev.printes.poll.model.enums.VotingEnum;
import dev.printes.poll.model.enums.VotingPermissionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PollFacade {

    @Value("${poll.session.result.queue}")
    private String pollSessionResultQueue;

    private final PollService service;
    private final PollValidationService validation;
    private final RabbitTemplate messaging;
    private final ValidatorClient validatorClient;

    public Long createPoll(PollRequestDTO dto) {
        return service.createPoll(PollMapper.toPollEntity(dto)).getId();
    }

    public void createPollSession(Long pollId, String closedDate) {
        var poll = service.findPollWithSessions(pollId);
        validation.checkConflictCurrentSession(poll);
        service.createSession(PollMapper.toPollSessionEntity(validation.checkClosedDate(closedDate), poll));
    }

    public void registerVoting(Long pollId, String vote) {
        var poll = service.findPollWithSessions(pollId);
        var currentSession = validation.checkRequiredCurrentSession(poll);

        var associate = this.findAssociate();
        var voting = service.findVotingWithAssociateByPollSession(currentSession.getId());
        var votingPermission = findVotingPermissionApi(associate);

        validation.checkVoting(voting, associate, vote, votingPermission);

        service.registerVoting(Voting.builder()
            .pollSession(currentSession)
            .associate(associate)
            .vote(VotingEnum.getOption(vote))
            .build());
    }

    private VotingPermissionEnum findVotingPermissionApi(Associate associate) {
        try {
            var response = validatorClient.validateCpf(associate.getCpf());
            if (response == null || !response.containsKey("status")) {
                return VotingPermissionEnum.UNABLE_TO_VOTE;
            }
            return VotingPermissionEnum.valueOf(response.get("status"));
        } catch (Exception e) {
            log.error("Error to validate Associate: {}", e.getMessage());
            return VotingPermissionEnum.UNABLE_TO_VOTE;
        }
    }

    public void closePollSession(Long pollId) {
        var poll = service.findPollWithSessions(pollId);
        var currentSession = validation.checkRequiredCurrentSession(poll);
        String emailAssociate = this.findAssociate().getEmail();
        service.closeSession(currentSession.getId(), LocalDateTime.now(), emailAssociate);
        this.sendSessionToCalculateResult(currentSession.getId(), emailAssociate);
    }

    public Page<PollResultDTO> getPollResult(int page, int size, String sortBy, String sortDir) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        var sessions = service.findPollLastSessionWithResult(pageable);
        var result = sessions.stream()
            .map(session -> new PollResultDTO(
                session.getPoll().getQuestion(),
                formatDate(session.getCreatedDate()),
                formatDate(session.getClosedDate()),
                session.getVoting().size(),
                this.getOptionResult(session)))
            .toList();
        return new PageImpl<>(result, pageable, result.size());
    }

    private String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    private Associate findAssociate() {
        return (Associate) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private String getOptionResult(PollSession session) {
        return Stream.of(VotingEnum.values())
            .filter(votingOption -> votingOption.name().equals(session.getResult()))
            .map(VotingEnum::getValue)
            .findAny()
            .orElseGet(() -> ResultEnum.valueOf(session.getResult()).getValue());
    }

    public void findSessionsToCalculateResult() {
        var sessionIds = service.findPollLastSessionWithoutResult().stream()
            .map(PollSession::getId)
            .toList();
        if (sessionIds.isEmpty()) {
            log.info("No session to calculate result.");
        } else {
            sessionIds.forEach(id -> this.sendSessionToCalculateResult(id, "system"));
        }
    }

    private void sendSessionToCalculateResult(Long sessionId, String closedBy) {
        try {
            var message = new PollMessageDTO(sessionId, closedBy);
            log.info("Message: {}",  message);
            messaging.convertAndSend(pollSessionResultQueue, message, new CorrelationData(sessionId.toString()));
        } catch (Exception e) {
            log.error("Send message error: {}", e.getMessage(), e);
        }
    }

}
