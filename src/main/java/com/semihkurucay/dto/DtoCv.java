package com.semihkurucay.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoCv extends DtoBaseEntity {

    private String bio;
    private List<DtoCvLanguage> languageCv;
    private List<DtoCvSchool> schoolCv;
    private List<DtoCvExperience> experienceCv;
    private DtoUser user;
}
