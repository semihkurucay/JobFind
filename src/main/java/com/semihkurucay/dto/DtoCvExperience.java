package com.semihkurucay.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DtoCvExperience extends DtoBaseEntity {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
