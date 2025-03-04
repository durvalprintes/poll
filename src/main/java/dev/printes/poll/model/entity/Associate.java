package dev.printes.poll.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "associate")
public class Associate extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "associateSequence")
    @SequenceGenerator(name = "associateSequence", sequenceName = "sq_associate", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "key", length = 36, nullable = false, unique = true)
    private UUID apiKey;
}
