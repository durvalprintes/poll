package dev.printes.poll.mapper;

import java.time.LocalDateTime;

import dev.printes.poll.model.dto.PollRequestDTO;
import dev.printes.poll.model.entity.Poll;

public class PollMapper {

    private PollMapper() {}

    public static Poll toPollEntity(PollRequestDTO dto) {
        return Poll.builder()
            .question(dto.question())
            .closedDate(generateClosedDate(dto.getClosedDateAsLocalDateTime()))
            .build();
    }

    private static LocalDateTime generateClosedDate(LocalDateTime closedDate) {
        return closedDate != null ? closedDate : LocalDateTime.now().plusMinutes(1L);
    }


}
