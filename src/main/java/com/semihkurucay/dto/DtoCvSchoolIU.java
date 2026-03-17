package com.semihkurucay.dto;

import com.semihkurucay.enums.SchoolType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DtoCvSchoolIU {

    @NotEmpty(message = "Okul adı bölümü boş geçilemez.")
    private String schoolName;
    private String description;
    private SchoolType schoolType;

    @Min(value = 0, message = "Okul puan bölümü en az 0 olabilir.")
    private Double point;

    @NotNull(message = "Okul başlangıç tarihi boş geçilemez.")
    private LocalDate startDate;
    private LocalDate endDate;
}
