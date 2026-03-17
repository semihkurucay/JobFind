package com.semihkurucay.dto;

import com.semihkurucay.enums.LanguageLevel;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoCvLanguageIU {

    @NotEmpty(message = "Yabancı dil adı bölümü boş geçilemez.")
    private String name;
    private LanguageLevel level;
}
