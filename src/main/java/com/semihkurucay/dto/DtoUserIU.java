package com.semihkurucay.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DtoUserIU {

    @NotEmpty(message = "İsim alanı boş geçilemez.")
    @Pattern(regexp = "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", message = "Sadece harf girin isim alanına.")
    private String firstName;

    @NotEmpty(message = "Soyisim alanı boş geçilemez.")
    @Pattern(regexp = "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", message = "Sadece harf girin soyisim alanına.")
    private String lastName;
    private String phone;
    private LocalDate birthDate;
    private String password;
    private DtoAddressIU address;
}
