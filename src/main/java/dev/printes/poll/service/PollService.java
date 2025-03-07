package dev.printes.poll.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dev.printes.poll.exception.ConflictException;
import dev.printes.poll.exception.ValidationException;
import dev.printes.poll.mapper.PollMapper;
import dev.printes.poll.model.dto.PollRequestDTO;
import dev.printes.poll.model.entity.Associate;
import dev.printes.poll.model.entity.Poll;
import dev.printes.poll.model.entity.PollSession;
import dev.printes.poll.model.entity.Voting;
import dev.printes.poll.model.enums.VotingEnum;
import dev.printes.poll.repository.PollRepository;
import dev.printes.poll.repository.PollSessionRepository;
import dev.printes.poll.repository.VotingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;
    private final PollSessionRepository pollSessionRepository;
    private final VotingRepository votingRepository;

    public Long createPoll(PollRequestDTO dto) {
        return pollRepository
            .save(PollMapper.toPollEntity(dto))
            .getId();
    }

    public void createPollSession(Long pollId, String closedDate) {
        Poll poll = this.findPollWithSessions(pollId);

        if (poll.getCurrentSession() != null) {
            throw new ConflictException("Já existe uma sessão de votação em andamento para a Pauta");
        }

        pollSessionRepository.save(PollMapper.toPollSessionEntity(poll, checkClosedDate(closedDate)));
    }

    private Poll findPollWithSessions(Long id) {
        var poll = pollRepository
            .findPollWithSessions(id)
            .orElseThrow(() -> new ValidationException("Pauta não encontrada"));
        poll.updateCurrentSession();
        poll.updateLastSession();
        return poll;
    }

    private LocalDateTime checkClosedDate(String closedDate) {
        if (closedDate != null) {
            LocalDateTime parsedDate = null;
            try {
                parsedDate = LocalDateTime.parse(closedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (Exception ex) {
                throw new ValidationException(String.format("O valor '%s' está incorreto, formato válido: yyyy-MM-dd HH:mm", closedDate));
            }
            if (parsedDate.isBefore(LocalDateTime.now())) {
                throw new ValidationException("A data e horário de encerramento deve ser futura, maior que o presente");
            }
            return parsedDate;
        }
        return LocalDateTime.now().plusMinutes(1L);
    }

    public void registerVoting(Long pollId, String vote) {
        var poll = this.findPollWithSessions(pollId);
        var currentSession = this.checkCurrentSession(poll);

        Associate associate = (Associate) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var voting = this.findVotingWithAssociateByPollSession(currentSession.getId());

        if (voting.stream().anyMatch(register -> register.getAssociate().getId().equals(associate.getId()))) {
            throw new ConflictException("Voto já foi registrado para esse Associado.");
        }

        if (!VotingEnum.isOption(vote)) {
            throw new ValidationException("Opção para votação incorreta, opçães válidas: SIM e NAO");
        }

        votingRepository.save(Voting.builder()
            .pollSession(currentSession)
            .associate(associate)
            .vote(VotingEnum.getOption(vote))
            .build());
    }

    public void closePollSession(Long pollId) {
        var poll = this.findPollWithSessions(pollId);
        var currentSession = this.checkCurrentSession(poll);
        currentSession.setClosedDate(LocalDateTime.now());
        pollSessionRepository.save(currentSession);
    }

    public Map<String, String> getPollResult(Long pollId) {
        var poll = this.findPollWithSessions(pollId);
        PollSession currentSession = null;
        try {
            currentSession = this.checkCurrentSession(poll);
        } catch (Exception e) {
            log.warn("Result Poll ID {}: {}", pollId, e.getMessage());
        }

        if (currentSession != null) {
            throw new ConflictException("A ultima sessão de votação está em andamento para a Pauta");
        }

        var lastSession = this.checkLastSession(poll);
        var session = this.findPollSessionWithVoting(lastSession.getId());
        return Map.of("resultado", session.getResult());
    }

    private PollSession findPollSessionWithVoting(Long id) {
        var session = pollSessionRepository
            .findPollSessionWithVoting(id)
            .orElseThrow(() -> new ValidationException("Sessão não encontrada"));
        session.calculateResult();
        return session;
    }

    private List<Voting> findVotingWithAssociateByPollSession(Long id) {
        return votingRepository.findVotingWithAssociateByPollSession(id);
    }

    private PollSession checkCurrentSession(Poll poll) {
        return Optional.ofNullable(poll.getCurrentSession())
            .orElseThrow(() -> new ConflictException("Não existe sessão aberta para a Pauta"));
    }

    private PollSession checkLastSession(Poll poll) {
        return Optional.ofNullable(poll.getLastSession())
            .orElseThrow(() -> new ConflictException("Não existe sessão encerrada para a Pauta"));
    }



}
