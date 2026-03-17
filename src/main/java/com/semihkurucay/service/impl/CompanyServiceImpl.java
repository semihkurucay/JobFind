package com.semihkurucay.service.impl;

import com.semihkurucay.dto.*;
import com.semihkurucay.entity.Address;
import com.semihkurucay.entity.Company;
import com.semihkurucay.entity.CompanyComment;
import com.semihkurucay.entity.User;
import com.semihkurucay.exception.BaseException;
import com.semihkurucay.exception.ErrorMessage;
import com.semihkurucay.exception.ErrorType;
import com.semihkurucay.mapper.AddressMapper;
import com.semihkurucay.mapper.CompanyCommentMapper;
import com.semihkurucay.mapper.CompanyMapper;
import com.semihkurucay.repository.CompanyCommentRepository;
import com.semihkurucay.repository.CompanyRepository;
import com.semihkurucay.repository.UserRepository;
import com.semihkurucay.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyCommentRepository companyCommentRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;
    private final CompanyCommentMapper companyCommentMapper;
    private final AddressMapper addressMapper;

    @Override
    public DtoPublicCompanyInfo findByLogin_Username(String username) {

        Company company = companyRepository.findByLogin_Username(username)
                .orElseThrow(() -> new BaseException(new ErrorMessage(username, ErrorType.NO_RECORD_EXIT)));

        return companyMapper.toDtoPublicInfo(company);
    }

    @Transactional
    @Override
    public DtoCompany update(String username, DtoCompanyIU dtoCompanyIU) {

        Company company = companyRepository.findByLogin_Username(username)
                .orElseThrow(() -> new BaseException(new ErrorMessage(username, ErrorType.NO_RECORD_EXIT)));

        if(company.getAddress() == null){
            company.setAddress(new Address());
        }

        company.setAddress(addressMapper.toUpdated(dtoCompanyIU.getAddress() , company.getAddress()));
        company = companyMapper.toUpdated(dtoCompanyIU, company);

        return companyMapper.toDtoCompany(companyRepository.save(company));
    }

    private CompanyComment getCompanyComment(String username, Long companyId) {
        CompanyComment companyComment = companyCommentRepository.findByUserLogin_UsernameAndCompany_Id(username, companyId);
        return companyComment == null ? new CompanyComment() : companyComment;
    }

    private void setCompanyAvaregePoint(Company company){
        company.setAveragePoint(companyCommentRepository.getAveragePointByCompany_Id(company.getId()));
        companyRepository.save(company);
    }

    @Transactional
    @Override
    public DtoPublicCompanyComment sendCompanyComment(Long companyId, String username, DtoCompanyCommentIU dtoCompanyCommentIU) {
        CompanyComment companyComment =  companyCommentMapper.toUpdated(dtoCompanyCommentIU, getCompanyComment(username, companyId));
        companyComment.setCreatedDate(LocalDate.now());

        if(companyComment.getCompany() == null){
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new BaseException(new ErrorMessage(companyId.toString(), ErrorType.NO_RECORD_EXIT)));

            companyComment.setCompany(company);
        }

        if(companyComment.getUser() == null){
            User user = userRepository.findByLogin_Username(username)
                    .orElseThrow(() -> new BaseException(new ErrorMessage(username, ErrorType.NO_RECORD_EXIT)));

            companyComment.setUser(user);
        }

        DtoPublicCompanyComment dtoPublicCompanyComment = companyCommentMapper.toDtoPublicCompanyComment(companyCommentRepository.save(companyComment));
        dtoPublicCompanyComment.setUserFirtLastName(companyComment.getUser().getFirstName() + " " + companyComment.getUser().getLastName());

        setCompanyAvaregePoint(companyComment.getCompany());

        return dtoPublicCompanyComment;
    }
}
