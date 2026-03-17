package com.semihkurucay.repository;

import com.semihkurucay.entity.CvSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CvSchoolRepository extends JpaRepository<CvSchool,Long> {

    @Modifying
    @Query("DELETE FROM CvSchool cv WHERE cv.id = :id AND cv.cv.user.login.username = :username")
    public void deleteById_AndCv_User_Login_Username(String username, Long id);
}
