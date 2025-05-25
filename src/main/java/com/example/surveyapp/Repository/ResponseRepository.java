package com.example.surveyapp.Repository;

import com.example.surveyapp.Entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response,Long> {
    List<Response> findBySurveyId(Long surveyId);
    List<Response> findByUserId(Long userId);
}
