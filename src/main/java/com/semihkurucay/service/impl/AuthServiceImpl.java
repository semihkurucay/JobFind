package com.semihkurucay.service.impl;

import com.semihkurucay.dto.*;
import com.semihkurucay.entity.Company;
import com.semihkurucay.entity.Login;
import com.semihkurucay.entity.RefreshToken;
import com.semihkurucay.entity.User;
import com.semihkurucay.exception.BaseException;
import com.semihkurucay.exception.ErrorMessage;
import com.semihkurucay.exception.ErrorType;
import com.semihkurucay.jwt.JwtService;
import com.semihkurucay.mapper.CompanyMapper;
import com.semihkurucay.mapper.UserMapper;
import com.semihkurucay.repository.CompanyRepository;
import com.semihkurucay.repository.LoginRepository;
import com.semihkurucay.repository.RefreshTokenRepository;
import com.semihkurucay.repository.UserRepository;
import com.semihkurucay.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final LoginRepository loginRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;

    private RefreshToken createRefreshToken(Login login) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setLogin(login);
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpiration(LocalDateTime.now().plusHours(4));
        return refreshToken;
    }

    @Transactional
    @Override
    public DtoAuthResponse login(DtoAuthRequest dtoAuthRequest) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(dtoAuthRequest.getUsername(), dtoAuthRequest.getPassword());
            authenticationProvider.authenticate(usernamePasswordAuthenticationToken);

            Login login = loginRepository.findByUsername(dtoAuthRequest.getUsername())
                    .orElseThrow(() -> new BaseException(new ErrorMessage(null, ErrorType.INCORRECT_USERNAME_OR_PASSWORD)));

            String accessToken = jwtService.generateToken(login);
            RefreshToken refreshToken = refreshTokenRepository.save(createRefreshToken(login));

            if(login.getUser() != null){
                DtoAuthResponse<DtoAuthUser> response = new DtoAuthResponse<>();
                response.setAccessToken(accessToken);
                response.setRefreshToken(refreshToken.getRefreshToken());
                response.setUser(userMapper.toDtoAuthUser(login.getUser()));
                response.getUser().setUsername(dtoAuthRequest.getUsername());

                return response;
            }else{
                DtoAuthResponse<DtoAuthCompany> response = new DtoAuthResponse<>();
                response.setAccessToken(accessToken);
                response.setRefreshToken(refreshToken.getRefreshToken());
                response.setUser(companyMapper.toDtoAuthCompany(login.getCompany()));
                response.getUser().setUsername(dtoAuthRequest.getUsername());

                return response;
            }
        } catch (Exception e) {
            throw new BaseException(new ErrorMessage(e.getMessage(), ErrorType.INCORRECT_USERNAME_OR_PASSWORD));
        }
    }

    private void checkUseUsername(String username){

        if(loginRepository.existsByUsername(username)){
            throw new BaseException(new ErrorMessage(null, ErrorType.USERNAME_IS_FOUND));
        }
    }

    private Login getLogin(String username, String password){
        Login login = new Login();
        login.setUsername(username);
        login.setPassword(bCryptPasswordEncoder.encode(password));

        return login;
    }

    @Transactional
    @Override
    public Boolean registerUser(DtoRegisterUser dtoRegisterUser) {

        checkUseUsername(dtoRegisterUser.getUsername());

        User user = new User();
        user.setFirstName(dtoRegisterUser.getFirstName());
        user.setLastName(dtoRegisterUser.getLastName());
        user.setLogin(getLogin(dtoRegisterUser.getUsername(), dtoRegisterUser.getPassword()));

        userRepository.save(user);

        return true;
    }

    @Transactional
    @Override
    public Boolean registerCompany(DtoRegisterCompany dtoRegisterCompany) {

        checkUseUsername(dtoRegisterCompany.getUsername());

        Company company = new Company();
        company.setName(dtoRegisterCompany.getName());
        company.setLogin(getLogin(dtoRegisterCompany.getUsername(), dtoRegisterCompany.getPassword()));

        companyRepository.save(company);

        return true;
    }
}
