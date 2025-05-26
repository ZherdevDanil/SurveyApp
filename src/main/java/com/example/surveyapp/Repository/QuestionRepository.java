package com.example.surveyapp.Repository;

import com.example.surveyapp.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findBySurveyIdOrderByPositionAsc(Long surveyId);
}
