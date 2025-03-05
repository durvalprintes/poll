package dev.printes.poll.repository;

import dev.printes.poll.model.entity.PollSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollSessionRepository extends JpaRepository<PollSession, Long> {
}
