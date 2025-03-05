package dev.printes.poll.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import dev.printes.poll.exception.PollException;
import dev.printes.poll.mapper.PollMapper;
import dev.printes.poll.model.dto.PollRequestDTO;
import dev.printes.poll.model.entity.Poll;
import dev.printes.poll.repository.PollRepository;
import dev.printes.poll.repository.PollSessionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;
    private final PollSessionRepository pollSessionRepository;

    public Long createPoll(PollRequestDTO dto) {
        return pollRepository
            .save(PollMapper.toPollEntity(dto))
            .getId();
    }

    public Long createPollSession(Long pollId, String closedDate) {
        Poll poll = pollRepository.findById(pollId)
            .orElseThrow(() -> new PollException("Pauta não encontrada"));
        //TODO: VERIFICAR SE JA EXISTE ALGUMA SESSAO ABERTA PARA A PAUTA
        return pollSessionRepository
            .save(PollMapper.toPollSessionEntity(poll, checkClosedDate(closedDate)))
            .getId();
    }

    private LocalDateTime checkClosedDate(String closedDate) {
        if (closedDate != null) {
            LocalDateTime parsedDate = null;
            try {
                parsedDate = LocalDateTime.parse(closedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (Exception ex) {
                throw new PollException(String.format("O valor '%s' está incorreto, formato válido: yyyy-MM-dd HH:mm", closedDate));
            }
            if (parsedDate.isBefore(LocalDateTime.now()))
                throw new PollException("A data e horário de encerramento deve ser futura, maior que o presente");
            return parsedDate;
        }
        return LocalDateTime.now().plusMinutes(1L);
    }

    public Object registerVoting(Long pollSessionId, String vote) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerVoting'");
    }

    public void closePollSession(Long pollId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closePollSession'");
    }

    public Object getPollResult(Long pollId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPollResult'");
    }

}
