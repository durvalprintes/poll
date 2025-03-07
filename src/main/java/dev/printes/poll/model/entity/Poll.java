package dev.printes.poll.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "poll")
public class Poll extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pollSequence")
    @SequenceGenerator(name = "pollSequence", sequenceName = "sq_poll", allocationSize = 1)
    private Long id;

    @Column(name = "question", length = 150, nullable = false, unique = true)
    private String question;

    @OneToMany(mappedBy = "poll")
    private List<PollSession> sessions;

    @Transient
    private PollSession currentSession;

    @Transient
    private PollSession lastSession;

    public void updateCurrentSession() {
        if (sessions == null || sessions.isEmpty()) {
            this.currentSession = null;
            return;
        }
        this.currentSession = sessions.stream()
            .filter(session -> session.getClosedDate().isAfter(LocalDateTime.now()))
            .findFirst()
            .orElse(null);
    }

    public void updateLastSession() {
        if (sessions == null || sessions.isEmpty()) {
            this.lastSession = null;
            return;
        }
        this.lastSession = sessions.stream()
            .filter(session -> session.getClosedDate().isBefore(LocalDateTime.now()))
            .max(Comparator.comparing(PollSession::getClosedDate))
            .orElse(null);
    }

}
