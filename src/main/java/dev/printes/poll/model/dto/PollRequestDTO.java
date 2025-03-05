package dev.printes.poll.model.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dev.printes.poll.validator.ValidFutureDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PollRequestDTO(
    @NotBlank(
        message = "A questão da pauta é obrigatória")
    @Size(
        max = 150,
        message = "O tamanho máximo da questão da pauta é de {max} caracteres")
    String question,

    @Pattern(
        regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) (0\\d|1\\d|2[0-3]):([0-5]\\d)$",
        message = "O valor '${validatedValue}' está incorreto, formato válido: yyyy-MM-dd HH:mm")
    @ValidFutureDateTime
    String closedDate
    ) {

    public LocalDateTime getClosedDateAsLocalDateTime() {
        return closedDate != null
            ? LocalDateTime.parse(closedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            : null;
    }

}
