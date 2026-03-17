package com.semihkurucay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoAuthUser extends DtoBaseEntity {

    private String username;
    private String firstName;
    private String lastName;
    private final String role = "SEEKER";
}
