package com.semihkurucay.dto;

import com.semihkurucay.enums.ApplyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoJobApplication extends DtoBaseEntity{

    private ApplyType applyType;
    private Long jobPostingId;
}
