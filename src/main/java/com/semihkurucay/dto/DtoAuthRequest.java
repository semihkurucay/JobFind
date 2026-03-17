package com.semihkurucay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoAuthRequest {

    @Email(message = "Geçersiz mail formatı.")
    private String username;
    private String password;
}
