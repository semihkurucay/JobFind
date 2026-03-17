package com.semihkurucay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoCompany {

    private String name;
    private String description;
    private Integer employeeCount;
    private DtoAddress address;
}
