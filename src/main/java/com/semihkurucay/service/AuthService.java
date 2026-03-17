package com.semihkurucay.service;

import com.semihkurucay.dto.DtoAuthRequest;
import com.semihkurucay.dto.DtoAuthResponse;
import com.semihkurucay.dto.DtoRegisterCompany;
import com.semihkurucay.dto.DtoRegisterUser;

public interface AuthService {

    public DtoAuthResponse login(DtoAuthRequest dtoAuthRequest);
    public Boolean registerUser(DtoRegisterUser dtoRegisterUser);
    public Boolean registerCompany(DtoRegisterCompany dtoRegisterCompany);
}
