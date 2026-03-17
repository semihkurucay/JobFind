package com.semihkurucay.service;

import com.semihkurucay.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublicStatusService {

    public DtoPublicStatus getStatus();
    public Page<DtoPublicCompany> publicFindAllCompanyByName(String search, Pageable pageable);
    public DtoPublicCompanyInfo publicFindCompanyById(Long id);
    public Page<DtoPublicCompanyComment> publicFindAllCompanyCommentByCompany_Id(Long companyId, Pageable pageable);
    public Page<DtoPublicJobPosting> publicFindAllJobPostingByCompany_Id(Long companyId, Pageable pageable);
    public Page<DtoPublicJobPosting> publicFindAllPosting(Pageable pageable);
}
