package com.semihkurucay.entity;

import com.semihkurucay.enums.LanguageLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "language_cv")
@Getter
@Setter
public class CvLanguage extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "level", nullable = false)
    @Enumerated(EnumType.STRING)
    private LanguageLevel level;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
