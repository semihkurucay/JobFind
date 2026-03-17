package com.semihkurucay.enums;

import lombok.Getter;

@Getter
public enum SchoolType {
    PRIMARY_SCHOOL("İlkokul"),
    MIDDLE_SCHOOL("Ortaokul"),
    HIGH_SCHOOL("Lise"),
    ASSOCIATE("Ön Lisans"),
    BACHELOR("Lisans"),
    MASTER("Yüksek Lisans");

    private String schoolType;

    SchoolType(String schoolType) {
        this.schoolType = schoolType;
    }
}
