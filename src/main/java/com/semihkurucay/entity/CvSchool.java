package com.semihkurucay.entity;

import com.semihkurucay.enums.SchoolType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "school_cv")
@Getter
@Setter
public class CvSchool extends BaseEntity {

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(name = "description")
    private String description;

    @Column(name = "school_type")
    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    @Column(name = "point")
    private Double point;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
