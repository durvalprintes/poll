package dev.printes.poll.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.printes.poll.model.entity.PollSession;

public interface PollSessionRepository extends JpaRepository<PollSession, Long> {

    @EntityGraph(attributePaths = {"voting"})
    @Query("SELECT s FROM PollSession s WHERE s.id = :id")
    Optional<PollSession> findPollSessionWithVoting(@Param("id") Long id);

    @Query("""
        SELECT s FROM PollSession s
        WHERE s.closedDate < CURRENT_TIMESTAMP
        AND s.result IS NULL
        AND s.id IN (
            SELECT MAX(ps.id) FROM PollSession ps
            WHERE ps.closedDate < CURRENT_TIMESTAMP
            AND ps.result IS NULL
            GROUP BY ps.poll)
        """)
    List<PollSession> findPollLastSessionWithoutResult();

    @EntityGraph(attributePaths = {"poll"})
    @Query("""
        SELECT s FROM PollSession s
        WHERE s.closedDate < CURRENT_TIMESTAMP
        AND s.result IS NOT NULL
        AND s.id IN (
            SELECT MAX(ps.id) FROM PollSession ps
            WHERE ps.closedDate < CURRENT_TIMESTAMP
            AND ps.result IS NOT NULL
            GROUP BY ps.poll)
        """)
    Page<PollSession> findPollLastSessionWithResult(Pageable pageable);

    @Modifying
    @Query("UPDATE PollSession s SET s.result = :result, s.modifiedBy = 'system', s.modifiedDate = CURRENT_TIMESTAMP WHERE s.id = :id")
    void saveSessionResult(Long id, String result);

    @Modifying
    @Query("UPDATE PollSession s SET s.closedDate = :closedDate, s.modifiedBy = :associate, s.modifiedDate = CURRENT_TIMESTAMP WHERE s.id = :id")
    void closeSession(Long id, @Param("closedDate") LocalDateTime closedDate, @Param("associate") String associate);

}
