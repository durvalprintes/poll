package dev.printes.poll.config;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import dev.printes.poll.model.entity.Associate;

@Component
public class Auditor implements AuditorAware<String> {

    @SuppressWarnings("null")
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auditor = "system";

        if (authentication != null && authentication.isAuthenticated()) {
            var principal = authentication.getPrincipal();

            if (principal instanceof Associate associate && Objects.nonNull(associate.getEmail())) {
                auditor = associate.getEmail();
            } else if (principal instanceof UserDetails user && Objects.nonNull(user.getUsername())) {
                auditor = user.getUsername();
            }
        }

        return Optional.of(auditor);
    }

}

