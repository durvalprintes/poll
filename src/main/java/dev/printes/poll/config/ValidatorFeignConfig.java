package dev.printes.poll.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import dev.printes.poll.exception.ValidationException;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;

@Configuration
public class ValidatorFeignConfig {

    @Bean
    ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            HttpStatus status = HttpStatus.valueOf(response.status());
            return switch (status) {
                case NOT_FOUND -> new ValidationException("Invalid CPF.");
                case UNAUTHORIZED, FORBIDDEN -> new ValidationException("Invalid Token.");
                case INTERNAL_SERVER_ERROR -> new ValidationException("Internal error in the validation API.");
                default -> new ValidationException(response.reason());
            };
        };
    }

    @Bean
    RequestInterceptor requestInterceptor(@Value("${api.validator.token}") String token) {
        return template -> template.header("token", token);
    }
}
