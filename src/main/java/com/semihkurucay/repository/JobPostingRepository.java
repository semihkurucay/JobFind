package com.semihkurucay.repository;

import com.semihkurucay.dto.DtoJobPosting;
import com.semihkurucay.dto.DtoPublicJobPosting;
import com.semihkurucay.dto.DtoPublicStatus;
import com.semihkurucay.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    @Query("SELECT new com.semihkurucay.dto.DtoPublicStatus(" +
            "CAST(-1 AS long), " +
            "COUNT(DISTINCT jp.id), " +
            "COUNT(ja.id), " +
            "COUNT(CASE WHEN ja.applyType = com.semihkurucay.enums.ApplyType.APPROVED THEN 1 ELSE NULL END)) " +
            "FROM JobPosting jp " +
            "JOIN jp.company c " +
            "LEFT JOIN jp.jobApplication ja")
    public DtoPublicStatus getStatus();

    @Query("SELECT new com.semihkurucay.dto.DtoPublicJobPosting(jp.id, jp.count, jp.description, jp.endDate, jp.company.id, jp.company.name)" +
            "FROM JobPosting jp " +
            "WHERE jp.company.id = :companyId AND jp.endDate >= :date")
    public Page<DtoPublicJobPosting> publicFindAllJobPostingByCompany_Id(Long companyId, LocalDate date, Pageable pageable);

    @Query("SELECT new com.semihkurucay.dto.DtoPublicJobPosting(jp.id, jp.count, jp.description, jp.endDate, c.id, c.name)" +
            "FROM JobPosting jp " +
            "JOIN jp.company c " +
            "WHERE jp.endDate >= :date")
    public Page<DtoPublicJobPosting> publicFindAllPosting(LocalDate date, Pageable pageable);

    @Query("SELECT jp " +
            "FROM JobPosting jp " +
            "WHERE jp.company.id = :id")
    public Page<JobPosting> findAllPosting(Long id, Pageable pageable);

    public void deleteByIdAndCompanyId(Long id, Long companyId);
}
