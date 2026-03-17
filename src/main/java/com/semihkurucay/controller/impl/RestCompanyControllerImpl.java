package com.semihkurucay.controller.impl;

import com.semihkurucay.controller.RestCompanyController;
import com.semihkurucay.controller.RootEntity;
import com.semihkurucay.dto.*;
import com.semihkurucay.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class RestCompanyControllerImpl extends RestBaseController implements RestCompanyController {

    private final CompanyService companyService;

    @GetMapping("/me")
    @Override
    public RootEntity<DtoPublicCompanyInfo> findByLogin_Username(Principal principal) {
        return ok(companyService.findByLogin_Username(principal.getName()));
    }

    @PutMapping("/me")
    @Override
    public RootEntity<DtoCompany> update(@Valid @RequestBody DtoCompanyIU dtoCompanyIU, Principal principal) {
        return ok(companyService.update(principal.getName(), dtoCompanyIU));
    }

    @PutMapping("/{id}/comments")
    @Override
    public RootEntity<DtoPublicCompanyComment> sendCompanyComment(@PathVariable(name = "id") Long companyId, Principal principal, @Valid @RequestBody DtoCompanyCommentIU dtoCompanyCommentIU) {
        return ok(companyService.sendCompanyComment(companyId, principal.getName(), dtoCompanyCommentIU));
    }
}
