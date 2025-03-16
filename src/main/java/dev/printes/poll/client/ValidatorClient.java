package dev.printes.poll.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dev.printes.poll.config.ValidatorFeignConfig;

@FeignClient(name = "validator-api", url = "${api.validator.url}", configuration = ValidatorFeignConfig.class)
public interface ValidatorClient {

    @GetMapping("/users/{cpf}")
    Map<String, String> validateCpf(@PathVariable("cpf") String cpf);

}
