package dev.printes.poll.model.dto;

import java.util.List;

public record PollSesssionResultDTO(
    String startDate,
    String closedDate,
    int totalVoting,
    String result,
    List<PollVotingDTO> voting) {
}
