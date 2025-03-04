package dev.printes.poll.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Audit {
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private long createdDate;

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "modified_date")
    @LastModifiedDate
    private long modifiedDate;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;
}
