package dev.printes.poll.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private String result;

    public void calculateResult() {
        if (voting == null || voting.isEmpty()) {
            this.result = null;
            return;
        }

        var voteCount = voting.stream()
            .collect(Collectors.groupingBy(Voting::getVote, Collectors.counting()));

        var maxVotes = Collections.max(voteCount.values());

        var topVotes = voteCount.entrySet().stream()
            .filter(e -> e.getValue().equals(maxVotes))
            .map(Map.Entry::getKey)
            .toList();

        this.result = topVotes.size() > 1 ? "EMPATE" : topVotes.get(0).getValue();
    }
}
