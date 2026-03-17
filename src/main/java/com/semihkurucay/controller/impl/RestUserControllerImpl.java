package com.semihkurucay.controller.impl;

import com.semihkurucay.controller.RestUserController;
import com.semihkurucay.controller.RootEntity;
import com.semihkurucay.dto.*;
import com.semihkurucay.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class RestUserControllerImpl extends RestBaseController implements RestUserController {

    private final UserService userService;

    @GetMapping("/profile/me")
    @Override
    public RootEntity<DtoUser> findByLogin_Username(Principal principal) {
        return ok(userService.findByLogin_Username(principal.getName()));
    }

    @PutMapping("/profile/me")
    @Override
    public RootEntity<DtoUser> update(Principal principal, @Valid @RequestBody DtoUserIU dtoUserIU) {
        return ok(userService.update(principal.getName(), dtoUserIU));
    }

    @GetMapping("/cv/me")
    @Override
    public RootEntity<DtoCv> findByUser_Login_Username(Principal principal) {
        return ok(userService.findByUser_Login_Username(principal.getName()));
    }

    @GetMapping("/cv/user/{id}")
    @Override
    public RootEntity<DtoCv> cvFindById(@PathVariable(name = "id") Long id) {
        return ok(userService.findByUser_Id(id));
    }

    @PutMapping("/cv/bio")
    @Override
    public RootEntity<DtoCv> saveCv(Principal principal, @Valid @RequestBody DtoCvIU dtoCvIU) {
        return ok(userService.saveCv(principal.getName(), dtoCvIU));
    }

    @PostMapping("/cv/schools")
    @Override
    public RootEntity<DtoCvSchool> saveCvSchool(Principal principal, @Valid @RequestBody DtoCvSchoolIU dtoCvSchoolIU) {
        return ok(userService.saveCvSchool(principal.getName(), dtoCvSchoolIU));
    }

    @DeleteMapping("/cv/schools/{id}")
    @Override
    public RootEntity deleteCvSchool(Principal principal, @PathVariable(name = "id") Long id) {
        userService.deleteCvSchool(principal.getName(), id);
        return ok(null);
    }

    @PostMapping("/cv/languages")
    @Override
    public RootEntity<DtoCvLanguage> saveCvLanguage(Principal principal, @Valid @RequestBody DtoCvLanguageIU dtoCvLanguageIU) {
        return ok(userService.saveCvLanguage(principal.getName(), dtoCvLanguageIU));
    }

    @DeleteMapping("/cv/languages/{id}")
    @Override
    public RootEntity deleteCvLanguage(Principal principal, @PathVariable(name = "id") Long id) {
        userService.deleteCvLanguage(principal.getName(), id);
        return ok(null);
    }

    @PostMapping("/cv/experiences")
    @Override
    public RootEntity<DtoCvExperience> saveCvExperience(Principal principal, @Valid @RequestBody DtoCvExperienceIU dtoCvExperienceIU) {
        return ok(userService.saveCvExperience(principal.getName(), dtoCvExperienceIU));
    }

    @DeleteMapping("/cv/experiences/{id}")
    @Override
    public RootEntity deleteCvExperience(Principal principal, @PathVariable(name = "id") Long id) {
        userService.deleteCvExperience(principal.getName(), id);
        return ok(null);
    }
}
