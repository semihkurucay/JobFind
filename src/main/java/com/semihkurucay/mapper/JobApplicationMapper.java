package com.semihkurucay.mapper;

import com.semihkurucay.dto.DtoJobApplication;
import com.semihkurucay.dto.DtoJobApplicationCompany;
import com.semihkurucay.dto.DtoJobApplicationUser;
import com.semihkurucay.entity.JobApplication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {JobPostingMapper.class, UserMapper.class})
public interface JobApplicationMapper {

    DtoJobApplicationUser toDtoJobApplicationUser(JobApplication jobApplication);
    DtoJobApplicationCompany toDtoJobApplicationCompany(JobApplication jobApplication);
}
