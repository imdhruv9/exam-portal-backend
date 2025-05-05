package com.testportal.online_test_portal.repository;

import com.testportal.online_test_portal.entity.UserExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserExamRepository extends JpaRepository<UserExam,Long> {
    @Query("SELECT ue FROM UserExam ue WHERE ue.user.id = :userId")
    List<UserExam> findByUserId(@Param("userId") Long userId);

}
