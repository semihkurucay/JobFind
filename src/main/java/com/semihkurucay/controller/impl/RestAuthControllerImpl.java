package com.semihkurucay.controller.impl;

import com.semihkurucay.controller.RestAuthController;
import com.semihkurucay.controller.RootEntity;
import com.semihkurucay.dto.DtoAuthRequest;
import com.semihkurucay.dto.DtoAuthResponse;
import com.semihkurucay.dto.DtoRegisterCompany;
import com.semihkurucay.dto.DtoRegisterUser;
import com.semihkurucay.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class RestAuthControllerImpl extends RootEntity implements RestAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Override
    public RootEntity<DtoAuthResponse> authanticate(@RequestBody DtoAuthRequest request) {
        return ok(authService.login(request));
    }

    @PostMapping("/register/seeker")
    @Override
    public RootEntity<Boolean> registerUser(@Valid @RequestBody DtoRegisterUser user) {
        return ok(authService.registerUser(user));
    }

    @PostMapping("/register/employer")
    @Override
    public RootEntity<Boolean> registerCompany(@Valid @RequestBody DtoRegisterCompany company) {
        return ok(authService.registerCompany(company));
    }
}
