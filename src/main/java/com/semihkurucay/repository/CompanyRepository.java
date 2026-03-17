package com.semihkurucay.repository;

import com.semihkurucay.dto.DtoPublicCompany;
import com.semihkurucay.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByLogin_Username(String username);

    @Query(value = "SELECT new com.semihkurucay.dto.DtoPublicCompany(c.id, c.name, c.description, AVG(cc.point)) " +
            "FROM Company c " +
            "LEFT JOIN c.companyComment cc " +
            "WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:search), '%') " +
            "GROUP BY c.id, c.name, c.description",
            countQuery = "SELECT COUNT(c.id) FROM Company c WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:search), '%')")
    Page<DtoPublicCompany> publicFindAllCompanyByName(String search, Pageable pageable);
}
