package com.semihkurucay.repository;

import com.semihkurucay.entity.Cv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CvRepository extends JpaRepository<Cv, Long> {

    @Query("FROM Cv cv WHERE cv.user.login.username = :username")
    Optional<Cv> findByUser_Login_Username(String username);

    @Query("FROM Cv cv WHERE cv.user.id = :id")
    Optional<Cv> findByUser_Id(Long id);
}
