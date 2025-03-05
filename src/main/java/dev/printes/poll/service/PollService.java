package dev.printes.poll.service;

import org.springframework.stereotype.Service;

import dev.printes.poll.mapper.PollMapper;
import dev.printes.poll.model.dto.PollRequestDTO;
import dev.printes.poll.repository.PollRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;

    public Long createPoll(PollRequestDTO dto, String createdBy) {
        return pollRepository.save(PollMapper.toPollEntity(dto, createdBy)).getId();
    }

}
