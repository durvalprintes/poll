package dev.printes.poll.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PollRequestDTO(
    @NotBlank(
        message = "A questão da pauta é obrigatória")
    @Size(
        max = 150,
        message = "O tamanho máximo da questão da pauta é de {max} caracteres")
    String question) {
}
