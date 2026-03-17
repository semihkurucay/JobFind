package com.semihkurucay.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DtoJobPostingIU {

    @NotNull(message = "Alınacak kişi sayısı boş geçilemez.")
    @Min(value = 1, message = "Alınacak kişi sayısı en az 1 olmalı.")
    private Integer count;

    @NotEmpty(message = "Açıklama alanı boş geçilemez.")
    private String description;

    @NotNull(message = "Bitiş tarihi alanı boş geçilemez.")
    private LocalDate endDate;
}
