package dev.printes.poll.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.printes.poll.exception.ValidationException;
import dev.printes.poll.model.entity.Poll;
import dev.printes.poll.model.entity.PollSession;
import dev.printes.poll.model.entity.Voting;
import dev.printes.poll.repository.PollRepository;
import dev.printes.poll.repository.PollSessionRepository;
import dev.printes.poll.repository.VotingRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;
    private final PollSessionRepository pollSessionRepository;
    private final VotingRepository votingRepository;

    public Poll createPoll(Poll entity) {
        return pollRepository.save(entity);
    }

    public void createSession(PollSession entity) {
        pollSessionRepository.save(entity);
    }

    public void registerVoting(Voting entity) {
        votingRepository.save(entity);
    }

    public Poll findPollWithSessions(Long id) {
        var poll = pollRepository
            .findPollWithSessions(id)
            .orElseThrow(() -> new ValidationException("Pauta não encontrada"));
        poll.updateCurrentSession();
        poll.updateLastSession();
        return poll;
    }

    public PollSession findPollSessionWithVoting(Long id) {
        var session = pollSessionRepository
            .findPollSessionWithVoting(id)
            .orElseThrow(() -> new ValidationException("Sessão não encontrada"));
        session.calculateResult();
        return session;
    }

    public List<Voting> findVotingWithAssociateByPollSession(Long id) {
        return votingRepository.findVotingWithAssociateByPollSession(id);
    }

    public List<PollSession> findPollLastSessionWithoutResult() {
        return pollSessionRepository.findPollLastSessionWithoutResult();
    }

    public Page<PollSession> findPollLastSessionWithResult(Pageable pageable) {
        return pollSessionRepository.findPollLastSessionWithResult(pageable);
    }

    public void updateSessionResult(Long id, String result, String closedBy) {
        pollSessionRepository.saveSessionResult(id, result, closedBy);
    }

    public void closeSession(Long id, LocalDateTime closedDate, String byAssociate) {
        pollSessionRepository.closeSession(id, closedDate, byAssociate);
    }

}
