package com.semihkurucay.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoAddressIU {

    @NotEmpty(message = "Şehir alanı boş geçilemez.")
    @Pattern(regexp = "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", message = "Sadece harf girin şehir alanına.")
    private String city;

    @NotEmpty(message = "İlçe alanı boş geçilemez.")
    @Pattern(regexp = "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", message = "Sadece harf girin ilçe alanına.")
    private String district;

    @NotEmpty(message = "Mahalle alanı boş geçilemez.")
    @Pattern(regexp = "^[0-9A-Za-zçğıöşüÇĞİÖŞÜ ]+$", message = "Sadece harf ve sayı girin mahalle alanına.")
    private String neighborhood;

    private String street;
}
