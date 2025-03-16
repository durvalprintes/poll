package dev.printes.poll.model.dto;

public record PollResultDTO(
    String question,
    String startDate,
    String closedDate,
    int totalVoting,
    String lastResult,
    boolean hasOpenSession
    ) {
}
