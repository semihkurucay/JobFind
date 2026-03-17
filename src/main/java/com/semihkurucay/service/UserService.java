package com.semihkurucay.service;

import com.semihkurucay.dto.*;

public interface UserService {
    public DtoUser findByLogin_Username(String username);
    public DtoUser update(String username, DtoUserIU dtoUserIU);
    public DtoCv findByUser_Login_Username(String username);
    public DtoCv findByUser_Id(Long id);
    public DtoCv saveCv(String username, DtoCvIU dtoCvIU);
    public DtoCvSchool saveCvSchool(String username, DtoCvSchoolIU dtoCvSchoolIU);
    public void deleteCvSchool(String username, Long id);
    public DtoCvLanguage saveCvLanguage(String username, DtoCvLanguageIU dtoCvLanguageIU);
    public void deleteCvLanguage(String username, Long id);
    public DtoCvExperience saveCvExperience(String username, DtoCvExperienceIU dtoCvExperienceIU);
    public void deleteCvExperience(String username, Long id);
}
