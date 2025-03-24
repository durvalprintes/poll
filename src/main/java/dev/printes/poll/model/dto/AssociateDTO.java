package dev.printes.poll.model.dto;

import org.hibernate.validator.constraints.UUID;
import org.hibernate.validator.constraints.br.CPF;

import dev.printes.poll.model.validation.CreateValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AssociateDTO(
    @Null(message = "O ID não pode ser preenchido")
    String id,
    @NotBlank(message = "O nome é obrigatório", groups = CreateValidation.class)
    @Size(
        max = 100,
        message = "O tamanho máximo do nome é de {max} caracteres"
    )
    String name,
    @NotBlank(message = "O email é obrigatório", groups = CreateValidation.class)
    @Email(message = "O email é invalido")
    String email,
    @NotBlank(message = "A chave é obrigatória", groups = CreateValidation.class)
    @UUID(message = "A chave é invalida. Precisar estar no formato UUID")
    String key,
    @NotBlank(message = "O CPF é obrigatório", groups = CreateValidation.class)
    @CPF(message = "O CPF é invalido")
    String cpf
    ) {
}

