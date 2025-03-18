package dev.printes.poll.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import dev.printes.poll.client.ValidatorClient;
import dev.printes.poll.model.entity.Associate;
import dev.printes.poll.model.enums.VotingPermissionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PollClient {

    private final ValidatorClient validatorClient;

    @Cacheable(cacheNames = "cpf", key = "#associate.cpf")
    public VotingPermissionEnum findVotingPermissionApi(Associate associate) {
        log.info("Validating Associate: {}", associate.getCpf());
        try {
            var response = validatorClient.validateCpf(associate.getCpf());
            if (response == null || !response.containsKey("status")) {
                return VotingPermissionEnum.UNABLE_TO_VOTE;
            }
            return VotingPermissionEnum.valueOf(response.get("status"));
        } catch (Exception e) {
            log.error("Error to validate Associate: {}", e.getMessage());
            return VotingPermissionEnum.UNABLE_TO_VOTE;
        }
    }

}
