package com.semihkurucay.dto;

import com.semihkurucay.enums.ApplyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoJobApplicationUser extends DtoBaseEntity{

    private DtoJobPostingJobApplication jobPosting;
    private ApplyType applyType;
}
