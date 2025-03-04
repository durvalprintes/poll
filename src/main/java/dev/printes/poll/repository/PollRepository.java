package dev.printes.poll.repository;

import dev.printes.poll.model.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {
}
