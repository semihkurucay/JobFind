package com.semihkurucay.repository;

import com.semihkurucay.dto.DtoJobApplicationUser;
import com.semihkurucay.entity.JobApplication;
import com.semihkurucay.entity.JobPosting;
import com.semihkurucay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    @Query("FROM JobApplication app WHERE app.user.login.username = :username")
    Page<JobApplication> getUserJobApplications(Pageable pageable, String username);

    @Query("FROM JobApplication app WHERE app.jobPosting.id = :jobId AND app.jobPosting.company.login.username = :username")
    Page<JobApplication> getCompanyJobApplications(Pageable pageable, Long jobId, String username);

    @Query("FROM JobApplication app WHERE app.id = :id AND app.jobPosting.company.login.username = :username")
    Optional<JobApplication> findByIdAndCompanyUsername(Long id, String username);

    boolean existsByUserAndJobPosting(User user, JobPosting jobPosting);
}
