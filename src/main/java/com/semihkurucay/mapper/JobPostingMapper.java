package com.semihkurucay.mapper;

import com.semihkurucay.dto.DtoJobPosting;
import com.semihkurucay.dto.DtoJobPostingIU;
import com.semihkurucay.dto.DtoJobPostingJobApplication;
import com.semihkurucay.dto.DtoPublicJobPosting;
import com.semihkurucay.entity.JobPosting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface JobPostingMapper {

    public JobPosting toEntity(DtoJobPostingIU dtoJobPostingIU);
    public DtoJobPosting toDtoJobPosting(JobPosting jobPosting);
    public DtoPublicJobPosting toDtoPublicJobPosting(JobPosting jobPosting);
    public DtoJobPostingJobApplication toDtoJobPostingJobApplication(JobPosting jobPosting);
}
