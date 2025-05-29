package com.example.surveyapp.Repository;

import com.example.surveyapp.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findBySurveyIdOrderByPositionAsc(Long surveyId);

    List<Question> findBySurveyId(Long surveyId);

}
