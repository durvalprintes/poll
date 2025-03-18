package dev.printes.poll.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.printes.poll.exception.ConflictException;
import dev.printes.poll.exception.ValidationException;
import dev.printes.poll.model.entity.Associate;
import dev.printes.poll.model.entity.Poll;
import dev.printes.poll.model.entity.PollSession;
import dev.printes.poll.model.entity.Voting;
import dev.printes.poll.model.enums.VotingEnum;
import dev.printes.poll.model.enums.VotingPermissionEnum;

@Service
public class PollValidator {

    public LocalDateTime checkClosedDate(String closedDate) {
        if (closedDate != null) {
            LocalDateTime parsedDate = null;
            try {
                parsedDate = LocalDateTime.parse(closedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (Exception ex) {
                throw new ValidationException(String.format("O valor '%s' está incorreto, formato válido: yyyy-MM-dd HH:mm", closedDate));
            }
            if (parsedDate.isBefore(LocalDateTime.now())) {
                throw new ValidationException("A data e horário de encerramento deve ser futura, maior que o presente");
            }
            return parsedDate;
        }
        return LocalDateTime.now().plusMinutes(1L);
    }

    public void checkConflictCurrentSession(Poll poll) {
        if (poll.getCurrentSession() != null) {
            throw new ConflictException("Já existe uma sessão de votação aberta para a Pauta");
        }
    }

    public PollSession checkRequiredCurrentSession(Poll poll) {
        return Optional.ofNullable(poll.getCurrentSession())
            .orElseThrow(() -> new ConflictException("Não existe sessão aberta para a Pauta"));
    }

    public PollSession checkRequiredLastSession(Poll poll) {
        return Optional.ofNullable(poll.getLastSession())
            .orElseThrow(() -> new ConflictException("Não existe sessão encerrada para a Pauta"));
    }

    public void checkVoting(List<Voting> voting, Associate associate, String vote, VotingPermissionEnum permission) {
        if (permission.equals(VotingPermissionEnum.UNABLE_TO_VOTE)) {
            throw new ValidationException("Associado não está habilitado para votar.");
        }

        if (voting.stream().anyMatch(register -> register.getAssociate().getId().equals(associate.getId()))) {
            throw new ConflictException("Voto já foi registrado para esse Associado.");
        }

        if (!VotingEnum.isOption(vote)) {
            throw new ValidationException("Opção para votação incorreta, opçães válidas: SIM e NAO");
        }
    }

}
