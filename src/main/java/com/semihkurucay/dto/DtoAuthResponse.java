package com.semihkurucay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoAuthResponse <T> {
    private String accessToken;
    private String refreshToken;
    private T user;
}
