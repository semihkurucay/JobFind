package com.semihkurucay.service;

import com.semihkurucay.dto.DtoJobApplication;
import com.semihkurucay.dto.DtoJobApplicationCompany;
import com.semihkurucay.dto.DtoJobApplicationIU;
import com.semihkurucay.dto.DtoJobApplicationUser;
import com.semihkurucay.entity.JobApplication;
import com.semihkurucay.enums.ApplyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationService {

    public DtoJobApplicationUser joinJobApplication(String username, DtoJobApplicationIU dtoJobApplicationIU);
    public Page<DtoJobApplicationUser> getUserJobApplications(String username, Pageable pageable);
    public Page<DtoJobApplicationCompany> getCompanyJobApplications(String username, Long jobId, Pageable pageable);
    public DtoJobApplicationCompany setJobApplicationStatus(String username, Long id, ApplyType applyType);
}
