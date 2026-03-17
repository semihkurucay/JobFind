package com.semihkurucay.controller;

import com.semihkurucay.dto.*;

import java.security.Principal;

public interface RestCompanyController {

    public RootEntity<DtoPublicCompanyInfo> findByLogin_Username(Principal principal);
    public RootEntity<DtoCompany> update(DtoCompanyIU dtoCompanyIU, Principal principa);
    public RootEntity<DtoPublicCompanyComment> sendCompanyComment(Long companyId, Principal principal, DtoCompanyCommentIU dtoCompanyCommentIU);
}
