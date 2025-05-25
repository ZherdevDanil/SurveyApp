package com.example.surveyapp.Repository;

import com.example.surveyapp.Entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey,Long> {
    List<Survey> findByCreatorId(Long creatorId);
}
