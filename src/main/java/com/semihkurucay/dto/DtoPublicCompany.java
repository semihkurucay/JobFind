package com.semihkurucay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoPublicCompany extends DtoBaseEntity {

    private String name;
    private String description;
    private Double avgPoint;

    public DtoPublicCompany(Long id, String name, String description, Double avgPoint) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description == null || description.isEmpty() ? "Açıklama Yok" : description);
        this.setAvgPoint(avgPoint == null ? 0.0 : avgPoint);
    }
}
