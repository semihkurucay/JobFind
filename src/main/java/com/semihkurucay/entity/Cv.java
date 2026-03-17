package com.semihkurucay.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "cv")
@Getter
@Setter
public class Cv extends BaseEntity {

    @Column(name = "bio", nullable = false)
    private String bio;

    @OneToMany(mappedBy = "cv",  cascade = CascadeType.ALL)
    private List<CvSchool>  schoolCv;

    @OneToMany(mappedBy = "cv",  cascade = CascadeType.ALL)
    private List<CvLanguage>  languageCv;

    @OneToMany(mappedBy = "cv",  cascade = CascadeType.ALL)
    private List<CvExperience>  experienceCv;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
