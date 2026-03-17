package com.semihkurucay.service;

import com.semihkurucay.dto.*;

public interface CompanyService {

    public DtoPublicCompanyInfo findByLogin_Username(String username);
    public DtoCompany update(String username, DtoCompanyIU dtoCompanyIU);
    public DtoPublicCompanyComment sendCompanyComment(Long companyId, String username, DtoCompanyCommentIU dtoCompanyCommentIU);
}
