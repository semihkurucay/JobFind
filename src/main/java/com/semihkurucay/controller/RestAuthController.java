package com.semihkurucay.controller;

import com.semihkurucay.dto.DtoAuthRequest;
import com.semihkurucay.dto.DtoAuthResponse;
import com.semihkurucay.dto.DtoRegisterCompany;
import com.semihkurucay.dto.DtoRegisterUser;

public interface RestAuthController {

    public RootEntity<DtoAuthResponse> authanticate(DtoAuthRequest request);
    public RootEntity<Boolean> registerUser(DtoRegisterUser user);
    public RootEntity<Boolean> registerCompany(DtoRegisterCompany company);
}
