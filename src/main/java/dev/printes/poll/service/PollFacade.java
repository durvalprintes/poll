package dev.printes.poll.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dev.printes.poll.mapper.PollMapper;
import dev.printes.poll.model.dto.PollRequestDTO;
import dev.printes.poll.model.entity.Associate;
import dev.printes.poll.model.entity.Voting;
import dev.printes.poll.model.enums.VotingEnum;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PollFacade {

    private final PollService pollService;
    private final PollValidationService pollValidation;

    public Long createPoll(PollRequestDTO dto) {
        return pollService.createPoll(PollMapper.toPollEntity(dto)).getId();
    }

    public void createPollSession(Long pollId, String closedDate) {
        var poll = pollService.findPollWithSessions(pollId);
        pollValidation.checkConflictCurrentSession(poll);
        pollService.createOrUpdateSession(PollMapper.toPollSessionEntity(pollValidation.checkClosedDate(closedDate), poll));
    }

    public void registerVoting(Long pollId, String vote) {
        var poll = pollService.findPollWithSessions(pollId);
        var currentSession = pollValidation.checkRequiredCurrentSession(poll);

        Associate associate = findAssociate();
        var voting = pollService.findVotingWithAssociateByPollSession(currentSession.getId());

        pollValidation.checkVoting(voting, associate, vote);

        pollService.registerVoting(Voting.builder()
            .pollSession(currentSession)
            .associate(associate)
            .vote(VotingEnum.getOption(vote))
            .build());
    }

    public void closePollSession(Long pollId) {
        var poll = pollService.findPollWithSessions(pollId);
        var currentSession = pollValidation.checkRequiredCurrentSession(poll);
        currentSession.setClosedDate(LocalDateTime.now());
        pollService.createOrUpdateSession(currentSession);
    }

    public Map<String, String> getPollResult(Long pollId) {
        var poll = pollService.findPollWithSessions(pollId);
        pollValidation.checkResult(pollId, poll);

        var lastSession = pollValidation.checkRequiredLastSession(poll);
        var session = pollService.findPollSessionWithVoting(lastSession.getId());
        return Map.of("resultado", session.getResult());
    }

    private Associate findAssociate() {
        return (Associate) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
