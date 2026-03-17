package com.semihkurucay.controller;

import com.semihkurucay.dto.*;
import com.semihkurucay.utils.RestPageableEntity;
import com.semihkurucay.utils.RestPageableRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestPublicStatusController {

    public RootEntity<DtoPublicStatus> getStatus();
    public RootEntity<RestPageableEntity<DtoPublicCompany>> publicFindAllCompanyByName(String search, RestPageableRequest request);
    public RootEntity<DtoPublicCompanyInfo> publicFindCompanyById(Long id);
    public RootEntity<RestPageableEntity<DtoPublicCompanyComment>> publicFindAllCompanyCommentByCompany_Id(Long companyId, RestPageableRequest request);
    public RootEntity<RestPageableEntity<DtoPublicJobPosting>> publicFindAllJobPostingByCompany_Id(Long companyId, RestPageableRequest request);
    public RootEntity<RestPageableEntity<DtoPublicJobPosting>> publicFindAllPosting(RestPageableRequest request);
}
