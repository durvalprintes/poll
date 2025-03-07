package dev.printes.poll.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.printes.poll.model.entity.PollSession;

public interface PollSessionRepository extends JpaRepository<PollSession, Long> {

    @EntityGraph(attributePaths = {"voting"})
    @Query("SELECT s FROM PollSession s WHERE s.id = :id")
    Optional<PollSession> findPollSessionWithVoting(@Param("id") Long id);

}
