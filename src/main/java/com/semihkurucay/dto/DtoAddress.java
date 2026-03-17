package com.semihkurucay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoAddress /*extends DtoBaseEntity*/ {

    private String city;
    private String district;
    private String neighborhood;
    private String street;
}
