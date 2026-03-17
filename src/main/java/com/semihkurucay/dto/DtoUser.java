package com.semihkurucay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoUser {

    private String firstName;
    private String lastName;
    private String phone;
    private String username;
    private LocalDate birthDate;
    private DtoAddress address;
}
