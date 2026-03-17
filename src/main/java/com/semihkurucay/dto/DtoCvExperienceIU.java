package com.semihkurucay.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DtoCvExperienceIU {

    @NotEmpty(message = "Deneyim adı bölümü boş geçilemez.")
    private String name;

    @NotNull(message = "Deneyim başlangıç tarihi boş geçilemez.")
    private LocalDate startDate;
    private LocalDate endDate;
}
