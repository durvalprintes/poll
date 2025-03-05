package dev.printes.poll.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.printes.poll.repository.AssociateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssociateService implements UserDetailsService {

    private final AssociateRepository associateRepository;

    @Override
    public UserDetails loadUserByUsername(String apiKey) throws UsernameNotFoundException {
        return associateRepository.findByApiKey(UUID.fromString(apiKey))
            .orElseThrow(() -> new UsernameNotFoundException("Associate not found"));
    }
}
