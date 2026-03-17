package com.semihkurucay.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DtoPublicJobPosting extends DtoBaseEntity {

    private Integer count;
    private String description;
    private LocalDate endDate;
    private Long companyId;
    private String companyName;

    public DtoPublicJobPosting(Long id, Integer count, String description, LocalDate endDate, Long companyId, String companyName) {
        this.setId(id);
        this.setCount(count);
        this.setDescription(description);
        this.setEndDate(endDate);
        this.setCompanyId(companyId);
        this.setCompanyName(companyName);
    }
}
