package dev.printes.poll.model.entity;

import dev.printes.poll.model.enums.Options;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "voting", uniqueConstraints = @UniqueConstraint(columnNames = {"poll_id", "associate_id"}))
public class Voting extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "votingSequence")
    @SequenceGenerator(name = "votingSequence", sequenceName = "sq_voting", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @ManyToOne
    @JoinColumn(name = "associate_id", nullable = false)
    private Associate associate;

    @Enumerated(EnumType.STRING)
    private Options vote;
}
