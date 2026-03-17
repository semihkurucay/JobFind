package com.semihkurucay.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "company_comment", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "company_id"}))
@Getter
@Setter
public class CompanyComment extends BaseEntity {

    @Column(name = "point", nullable = false)
    private Integer point;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
