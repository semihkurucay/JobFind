package com.semihkurucay.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "job_posting")
@Getter
@Setter
public class JobPosting extends BaseEntity {

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "jobPosting")
    private List<JobApplication> jobApplication;
}
