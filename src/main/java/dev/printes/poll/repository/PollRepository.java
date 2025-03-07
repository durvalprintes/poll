package dev.printes.poll.repository;

import dev.printes.poll.model.entity.Poll;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PollRepository extends JpaRepository<Poll, Long> {

    @EntityGraph(attributePaths = {"sessions"})
    @Query("SELECT p FROM Poll p WHERE p.id = :id")
    Optional<Poll> findPollWithSessions(@Param("id") Long id);

}
