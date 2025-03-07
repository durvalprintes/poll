package dev.printes.poll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.printes.poll.model.entity.Voting;

public interface VotingRepository extends JpaRepository<Voting, Long> {

    @EntityGraph(attributePaths = {"associate"})
    @Query("SELECT v FROM Voting v JOIN FETCH v.pollSession s WHERE s.id = :id")
    List<Voting> findVotingWithAssociateByPollSession(@Param("id") Long id);

}
