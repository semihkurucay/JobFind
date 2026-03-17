package com.semihkurucay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoRegisterUser {

    @NotEmpty(message = "Mail alanı boş geçilemez.")
    @Email(message = "Geçersiz mail formatı.")
    private String username;

    @NotEmpty(message = "Şifre alanı boş geçilemez.")
    @Size(min = 4, max = 12, message = "Şifre için en az 4 ve en fazla 12 karakter girin,")
    private String password;

    @NotEmpty(message = "İsim alanı boş geçilemez.")
    @Pattern(regexp = "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", message = "Sadece harf girin isim alanına.")
    private String firstName;

    @NotEmpty(message = "Soyisim alanı boş geçilemez.")
    @Pattern(regexp = "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", message = "Sadece harf girin soyisim alanına.")
    private String lastName;
}
