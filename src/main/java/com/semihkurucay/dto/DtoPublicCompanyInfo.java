package com.semihkurucay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoPublicCompanyInfo extends DtoBaseEntity {

    private String name;
    private String description;
    private Integer employeeCount;
    private Double averagePoint;
    private DtoAddress address;
}
