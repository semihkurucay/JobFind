package com.semihkurucay.dto;

import com.semihkurucay.entity.CompanyComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoPublicCompanyComment extends DtoBaseEntity {

    private String userFirtLastName;
    private Integer point;
    private String comment;
    private LocalDate createdDate;

    public DtoPublicCompanyComment(Long id, String userFirtLastName, Integer point, String comment, LocalDate createdDate) {
        this.setId(id);
        this.setUserFirtLastName(userFirtLastName);
        this.setPoint(point);
        this.setComment(comment);
        this.setCreatedDate(createdDate);
    }
}
