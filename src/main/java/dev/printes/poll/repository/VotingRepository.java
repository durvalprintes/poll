package dev.printes.poll.repository;

import dev.printes.poll.model.entity.Voting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingRepository extends JpaRepository<Voting, Long> {
}
