package dev.printes.poll.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "poll_session")
public class PollSession extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pollSessionSequence")
    @SequenceGenerator(name = "pollSessionSequence", sequenceName = "sq_poll_session", allocationSize = 1)
    private Long id;

    @Column(name = "closed_date", nullable = false)
    private LocalDateTime closedDate;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @OneToMany(mappedBy = "pollSession")
    private List<Voting> voting;

    @Transient
    private Boolean result;
}
