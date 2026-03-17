package com.semihkurucay.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoCompanyIU {

    @NotEmpty(message = "Şirket adı boş geçilemez.")
    private String name;
    private String description;

    @Min(value = 0, message = "Şirket çalışan sayısı - olamaz.")
    private Integer employeeCount;
    private DtoAddressIU address;
}
