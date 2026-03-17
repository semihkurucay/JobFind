package com.semihkurucay.service.impl;

import com.semihkurucay.dto.*;
import com.semihkurucay.entity.Company;
import com.semihkurucay.entity.JobApplication;
import com.semihkurucay.entity.JobPosting;
import com.semihkurucay.entity.User;
import com.semihkurucay.enums.ApplyType;
import com.semihkurucay.exception.BaseException;
import com.semihkurucay.exception.ErrorMessage;
import com.semihkurucay.exception.ErrorType;
import com.semihkurucay.mapper.JobApplicationMapper;
import com.semihkurucay.repository.CompanyRepository;
import com.semihkurucay.repository.JobApplicationRepository;
import com.semihkurucay.repository.JobPostingRepository;
import com.semihkurucay.repository.UserRepository;
import com.semihkurucay.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;
    private final JobApplicationMapper jobApplicationMapper;

    private JobApplication createJobApplication(User user, JobPosting jobPosting) {
        return jobApplicationRepository.save(new JobApplication(ApplyType.PENDING, user, jobPosting));
    }

    @Transactional
    @Override
    public DtoJobApplicationUser joinJobApplication(String username, DtoJobApplicationIU dtoJobApplicationIU) {
        JobPosting jobPosting = jobPostingRepository.findById(dtoJobApplicationIU.getJobId())
                .orElseThrow(() -> new BaseException(new ErrorMessage(dtoJobApplicationIU.getJobId().toString(), ErrorType.NO_RECORD_EXIT)));

        User user = userRepository.findByLogin_Username(username)
                .orElseThrow(() -> new BaseException(new ErrorMessage(username, ErrorType.NO_RECORD_EXIT)));

        if(jobApplicationRepository.existsByUserAndJobPosting(user, jobPosting)){
            throw new BaseException(new ErrorMessage(null, ErrorType.REPEAT_JOB_APPLICATION));
        }

        if(LocalDate.now().isAfter(jobPosting.getEndDate())){
            throw new BaseException(new ErrorMessage(jobPosting.getEndDate().toString(), ErrorType.EXPIRED_JOB_POSTING));
        }

        return jobApplicationMapper.toDtoJobApplicationUser(createJobApplication(user, jobPosting));
    }

    @Override
    public Page<DtoJobApplicationUser> getUserJobApplications(String username, Pageable pageable) {
        return jobApplicationRepository.getUserJobApplications(pageable, username)
                .map(jobApplicationMapper::toDtoJobApplicationUser);
    }

    @Override
    public Page<DtoJobApplicationCompany> getCompanyJobApplications(String username, Long jobId, Pageable pageable) {
        return jobApplicationRepository.getCompanyJobApplications(pageable, jobId, username)
                .map(jobApplicationMapper::toDtoJobApplicationCompany);
    }

    @Transactional
    @Override
    public DtoJobApplicationCompany setJobApplicationStatus(String username, Long id, ApplyType applyType) {
        JobApplication jobApplication = jobApplicationRepository.findByIdAndCompanyUsername(id, username)
                .orElseThrow(() -> new BaseException(new ErrorMessage(id.toString() + " - " + username, ErrorType.NO_RECORD_EXIT)));

        jobApplication.setApplyType(applyType);

        return jobApplicationMapper.toDtoJobApplicationCompany(jobApplicationRepository.save(jobApplication));
    }
}
