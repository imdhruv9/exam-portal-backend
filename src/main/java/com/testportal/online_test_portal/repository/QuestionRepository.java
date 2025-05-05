package com.testportal.online_test_portal.repository;

import com.testportal.online_test_portal.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    @Query(value = "SELECT * FROM questions WHERE topic_id = :topicId ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("topicId") Long topicId, @Param("limit") Integer limit);

}
