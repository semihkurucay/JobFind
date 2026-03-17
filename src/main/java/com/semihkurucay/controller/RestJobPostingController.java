package com.semihkurucay.controller;

import com.semihkurucay.dto.DtoJobPosting;
import com.semihkurucay.dto.DtoJobPostingIU;
import com.semihkurucay.utils.RestPageableEntity;
import com.semihkurucay.utils.RestPageableRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface RestJobPostingController {

    public RootEntity<RestPageableEntity<DtoJobPosting>> findAllPosting(Principal principal, RestPageableRequest request);
    public RootEntity<DtoJobPosting> createJobPosting(Principal principal, DtoJobPostingIU dtoJobPostingIU);
    public RootEntity deleteJobPosting(Principal principal, Long id);
}
