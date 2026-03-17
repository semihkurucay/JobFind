package com.semihkurucay.dto;

import com.semihkurucay.enums.LanguageLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoCvLanguage extends DtoBaseEntity {

    private String name;
    private LanguageLevel level;
}
