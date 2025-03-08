package dev.printes.poll.mapper;

import java.time.LocalDateTime;

import dev.printes.poll.model.dto.PollRequestDTO;
import dev.printes.poll.model.entity.Poll;
import dev.printes.poll.model.entity.PollSession;

public class PollMapper {

    private PollMapper() {}

    public static Poll toPollEntity(PollRequestDTO dto) {
        return Poll.builder()
            .question(dto.question())
            .build();
    }

    public static PollSession toPollSessionEntity(LocalDateTime closedDate, Poll poll) {
        return PollSession.builder()
            .closedDate(closedDate)
            .poll(poll)
            .build();
    }

}
