package com.semihkurucay.dto;

import com.semihkurucay.enums.SchoolType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DtoCvSchool extends DtoBaseEntity {

    private String schoolName;
    private String description;
    private SchoolType schoolType;
    private Double point;
    private LocalDate startDate;
    private LocalDate endDate;
}
