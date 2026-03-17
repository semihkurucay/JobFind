package com.semihkurucay.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DtoJobPosting extends DtoBaseEntity {

    private Integer count;
    private String description;
    private LocalDate createdDate;
    private LocalDate endDate;
}
