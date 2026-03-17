package com.semihkurucay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoPublicStatus {

    private Long totalCompanies;
    private Long totalJobs;
    private Long totalApps;
    private Long totalApproved;
}
