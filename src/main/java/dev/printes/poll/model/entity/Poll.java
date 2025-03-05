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
@Table(name = "poll")
public class Poll extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pollSequence")
    @SequenceGenerator(name = "pollSequence", sequenceName = "sq_poll", allocationSize = 1)
    private Long id;

    @Column(name = "question", length = 150, nullable = false, unique = true)
    private String question;

    @Column(name = "closed_date", nullable = false)
    private LocalDateTime closedDate;

    @OneToMany(mappedBy = "poll")
    private List<Voting> voting;

    @Transient
    private Boolean result;
}
