package com.semihkurucay.mapper;

import com.semihkurucay.dto.*;
import com.semihkurucay.entity.Cv;
import com.semihkurucay.entity.CvExperience;
import com.semihkurucay.entity.CvLanguage;
import com.semihkurucay.entity.CvSchool;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CvMapper {

    Cv toEntity(DtoCvIU dtoCvIU);
    Cv toUpdated(DtoCvIU dtoCvIU, @MappingTarget Cv cv);
    DtoCv  toDto(Cv cv);

    CvExperience toEntity(DtoCvExperienceIU dtoCvExperienceIU);
    CvExperience toUpdated(DtoCvExperienceIU dtoCvExperienceIU, @MappingTarget CvExperience cv);
    DtoCvExperience toDto(CvExperience cv);

    CvLanguage toEntity(DtoCvLanguageIU dtoCvLanguageIU);
    CvLanguage toUpdated(DtoCvLanguageIU dtoCvLanguageIU, @MappingTarget CvLanguage cv);
    DtoCvLanguage toDto(CvLanguage cv);

    CvSchool toEntity(DtoCvSchoolIU dtoCvSchoolIU);
    CvSchool toUpdated(DtoCvSchoolIU dtoCvSchoolIU, @MappingTarget CvSchool cv);
    DtoCvSchool toDto(CvSchool cv);
}
