package com.semihkurucay.repository;

import com.semihkurucay.entity.CvLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CvLanguageRepository extends JpaRepository<CvLanguage,Long> {

    @Modifying
    @Query("DELETE FROM CvLanguage cv WHERE cv.id = :id AND cv.cv.user.login.username = :username")
    public void deleteById_AndCv_User_Login_Username(String username, Long id);
}
