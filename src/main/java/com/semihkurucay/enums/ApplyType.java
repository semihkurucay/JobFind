package com.semihkurucay.enums;

import lombok.Getter;

@Getter
public enum ApplyType {

    PENDING("Beklemede"),
    REJECTED("Rededildi"),
    APPROVED("Onaylandı");

    private String applyType;

    ApplyType(String applyType) {
        this.applyType = applyType;
    }
}
