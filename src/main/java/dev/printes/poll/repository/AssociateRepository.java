package dev.printes.poll.repository;

import dev.printes.poll.model.entity.Associate;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociateRepository extends JpaRepository<Associate, Long> {

    Optional<Associate> findByApiKey(UUID apiKey);

}
