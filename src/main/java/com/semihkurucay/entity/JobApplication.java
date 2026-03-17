package com.semihkurucay.entity;

import com.semihkurucay.enums.ApplyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_application", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "job_posting_id"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication extends BaseEntity {

    @Column(name = "apply")
    @Enumerated(EnumType.STRING)
    private ApplyType applyType;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_posting_id")
    private JobPosting  jobPosting;
}
