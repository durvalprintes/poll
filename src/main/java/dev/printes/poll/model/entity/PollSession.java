package dev.printes.poll.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.printes.poll.model.enums.ResultEnum;

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

    @Column(name = "result", nullable = false)
    private String result;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @OneToMany(mappedBy = "pollSession")
    private List<Voting> voting;

    public void calculateResult() {
        if (!ResultEnum.WAITING.name().equals(result)) {
            return;
        }

        if(voting == null || voting.isEmpty()) {
            this.result = ResultEnum.NO_VOTING.name();
            return;
        }

        var voteCount = voting.stream()
            .collect(Collectors.groupingBy(Voting::getVote, Collectors.counting()));

        var maxVotes = Collections.max(voteCount.values());

        var topVotes = voteCount.entrySet().stream()
            .filter(e -> e.getValue().equals(maxVotes))
            .map(Map.Entry::getKey)
            .toList();

        this.result = topVotes.size() > 1 ? ResultEnum.TIE.name() : topVotes.get(0).name();
    }
}
