package com.semihkurucay.controller;

import com.semihkurucay.dto.DtoJobApplicationCompany;
import com.semihkurucay.dto.DtoJobApplicationIU;
import com.semihkurucay.dto.DtoJobApplicationUser;
import com.semihkurucay.enums.ApplyType;
import com.semihkurucay.utils.RestPageableEntity;
import com.semihkurucay.utils.RestPageableRequest;

import java.security.Principal;
import java.util.Map;


public interface RestJobApplicationController {

    public RootEntity<DtoJobApplicationUser> joinJobApplication(Principal principal, DtoJobApplicationIU dtoJobApplicationIU);
    public RootEntity<RestPageableEntity<DtoJobApplicationUser>> getUserJobApplications(Principal principal, RestPageableRequest request);
    public RootEntity<RestPageableEntity<DtoJobApplicationCompany>> getCompanyJobApplications(Principal principal, Long jobId, RestPageableRequest request);
    public RootEntity<DtoJobApplicationCompany> setJobApplicationStatus(Principal principal, Long id, Map<String, ApplyType> statusBody);
}
