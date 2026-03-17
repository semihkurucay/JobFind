package com.semihkurucay.repository;

import com.semihkurucay.dto.DtoPublicCompanyComment;
import com.semihkurucay.entity.Company;
import com.semihkurucay.entity.CompanyComment;
import com.semihkurucay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyCommentRepository extends JpaRepository<CompanyComment, Long> {

    public Boolean existsByUserAndCompany(User user, Company company);

    @Query("SELECT new com.semihkurucay.dto.DtoPublicCompanyComment(cc.id, CONCAT(cc.user.firstName, ' ', cc.user.lastName), cc.point, cc.comment, cc.createdDate) FROM CompanyComment cc WHERE cc.company.id = :companyId")
    Page<DtoPublicCompanyComment> publicFindAllCompanyCommentByCompany_Id(Long companyId, Pageable pageable);

    @Query("FROM CompanyComment cc WHERE cc.user.login.username = :username AND cc.company.id = :companyId")
    CompanyComment findByUserLogin_UsernameAndCompany_Id(String username, Long companyId);

    @Query("SELECT AVG(cc.point) FROM CompanyComment cc WHERE cc.company.id = :companyId")
    Double getAveragePointByCompany_Id(Long companyId);
}
