package com.semihkurucay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoAuthCompany extends DtoBaseEntity {

    private String username;
    private String name;
    private final String role = "EMPLOYER";
}
