package dev.printes.poll.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PollMessageDTO(@NotNull Long sessionId, @NotBlank String closedBy) {
}
