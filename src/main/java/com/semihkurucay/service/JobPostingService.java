package com.semihkurucay.service;

import com.semihkurucay.dto.DtoJobPosting;
import com.semihkurucay.dto.DtoJobPostingIU;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobPostingService {

    public Page<DtoJobPosting> findAllPosting(String username, Pageable pageable);
    public DtoJobPosting createJobPosting(String username, DtoJobPostingIU dtoJobPostingIU);
    public void deleteJobPosting(String username, Long id);
}
