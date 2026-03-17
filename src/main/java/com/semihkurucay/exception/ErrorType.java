package com.semihkurucay.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    GENERAL_ERROR("0x000", "Genel bir hata oldu."),
    EXPIRED_TOKEN("0x001", "Token süreniz dolmuştur."),
    NO_RECORD_EXIT("0x002", "Kayıt bulunamadı."),
    INCORRECT_USERNAME_OR_PASSWORD("0x003","Hatalı giriş denemesi."),
    USERNAME_IS_FOUND("0x004", "Kullanıcı adı sistemde kayıtlı."),
    REPEAT_RECORDING("0x005", "Tekrarlayan bir kayıt yapmaya çalıştınız."),
    EXPIRED_JOB_POSTING("0x006", "İş ilanının tarihi geçmiştir."),
    REPEAT_JOB_APPLICATION("0x007", "İş ilanına daha önceden başvurdunuz."),
    ERROR_DATE("0X008", "Hatalı tarih girişi yaptınız.");

    private String code;
    private String message;

    ErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
