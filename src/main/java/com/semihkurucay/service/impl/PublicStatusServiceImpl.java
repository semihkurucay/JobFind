package com.semihkurucay.service.impl;

import com.semihkurucay.dto.*;
import com.semihkurucay.entity.Company;
import com.semihkurucay.exception.BaseException;
import com.semihkurucay.exception.ErrorMessage;
import com.semihkurucay.exception.ErrorType;
import com.semihkurucay.mapper.CompanyMapper;
import com.semihkurucay.repository.CompanyCommentRepository;
import com.semihkurucay.repository.CompanyRepository;
import com.semihkurucay.repository.JobPostingRepository;
import com.semihkurucay.service.PublicStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PublicStatusServiceImpl implements PublicStatusService {

    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;
    private final CompanyCommentRepository companyCommentRepository;
    private final CompanyMapper companyMapper;

    @Override
    public DtoPublicStatus getStatus() {
        DtoPublicStatus dtoPublicStatus = jobPostingRepository.getStatus();
        dtoPublicStatus.setTotalCompanies(companyRepository.count());
        return dtoPublicStatus;
    }

    @Override
    public Page<DtoPublicCompany> publicFindAllCompanyByName(String search, Pageable pageable) {
        return companyRepository.publicFindAllCompanyByName(search == null || search.isEmpty() ? "" : search, pageable);
    }

    @Override
    public DtoPublicCompanyInfo publicFindCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new BaseException(new ErrorMessage(id.toString(), ErrorType.NO_RECORD_EXIT)));

        return companyMapper.toDtoPublicInfo(company);
    }

    @Override
    public Page<DtoPublicCompanyComment> publicFindAllCompanyCommentByCompany_Id(Long companyId, Pageable pageable) {
        return companyCommentRepository.publicFindAllCompanyCommentByCompany_Id(companyId, pageable);
    }

    @Override
    public Page<DtoPublicJobPosting> publicFindAllJobPostingByCompany_Id(Long companyId, Pageable pageable) {
        return jobPostingRepository.publicFindAllJobPostingByCompany_Id(companyId, LocalDate.now(), pageable);
    }

    @Override
    public Page<DtoPublicJobPosting> publicFindAllPosting(Pageable pageable) {
        return jobPostingRepository.publicFindAllPosting(LocalDate.now(), pageable);
    }
}
