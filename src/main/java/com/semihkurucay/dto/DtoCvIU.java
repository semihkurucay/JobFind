package com.semihkurucay.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoCvIU {

    @NotEmpty(message = "Hakkında bölümü boş geçilemez.")
    @Size(min = 15, message = "En az 15 karakter girin")
    private String bio;
}
