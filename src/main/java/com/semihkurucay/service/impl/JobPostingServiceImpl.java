package com.semihkurucay.service.impl;

import com.semihkurucay.dto.DtoJobPosting;
import com.semihkurucay.dto.DtoJobPostingIU;
import com.semihkurucay.entity.Company;
import com.semihkurucay.entity.JobPosting;
import com.semihkurucay.exception.BaseException;
import com.semihkurucay.exception.ErrorMessage;
import com.semihkurucay.exception.ErrorType;
import com.semihkurucay.mapper.JobPostingMapper;
import com.semihkurucay.repository.CompanyRepository;
import com.semihkurucay.repository.JobPostingRepository;
import com.semihkurucay.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingMapper  jobPostingMapper;

    private Company getCompany(String username) {
        return companyRepository.findByLogin_Username(username)
                .orElseThrow(() -> new BaseException(new ErrorMessage(username, ErrorType.NO_RECORD_EXIT)));
    }

    @Override
    public Page<DtoJobPosting> findAllPosting(String username, Pageable pageable) {
        return jobPostingRepository.findAllPosting(getCompany(username).getId(), pageable)
                .map(jobPostingMapper::toDtoJobPosting);
    }

    @Transactional
    @Override
    public DtoJobPosting createJobPosting(String username, DtoJobPostingIU dtoJobPostingIU) {
        if(dtoJobPostingIU.getEndDate().isBefore(LocalDate.now())) {
            throw new BaseException(new ErrorMessage(dtoJobPostingIU.getEndDate().toString(), ErrorType.ERROR_DATE));
        }

        JobPosting jobPosting = jobPostingMapper.toEntity(dtoJobPostingIU);
        jobPosting.setCreatedDate(LocalDate.now());
        jobPosting.setCompany(getCompany(username));

        return jobPostingMapper.toDtoJobPosting(jobPostingRepository.save(jobPosting));
    }

    @Transactional
    @Override
    public void deleteJobPosting(String username, Long id) {
        jobPostingRepository.deleteByIdAndCompanyId(id, getCompany(username).getId());
    }
}
