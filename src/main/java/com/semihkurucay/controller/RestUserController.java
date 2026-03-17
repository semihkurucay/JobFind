package com.semihkurucay.controller;

import com.semihkurucay.dto.*;

import java.security.Principal;

public interface RestUserController {

    public RootEntity<DtoUser> findByLogin_Username(Principal principal);
    public RootEntity<DtoUser> update(Principal principal, DtoUserIU dtoUserIU);
    public RootEntity<DtoCv> findByUser_Login_Username(Principal principal);
    public RootEntity<DtoCv> cvFindById(Long id);
    public RootEntity<DtoCv> saveCv(Principal principal, DtoCvIU dtoCvIU);
    public RootEntity<DtoCvSchool> saveCvSchool(Principal principal, DtoCvSchoolIU dtoCvSchoolIU);
    public RootEntity deleteCvSchool(Principal principal, Long id);
    public RootEntity<DtoCvLanguage> saveCvLanguage(Principal principal, DtoCvLanguageIU dtoCvLanguageIU);
    public RootEntity deleteCvLanguage(Principal principal, Long id);
    public RootEntity<DtoCvExperience> saveCvExperience(Principal principal, DtoCvExperienceIU dtoCvExperienceIU);
    public RootEntity deleteCvExperience(Principal principal, Long id);
}
