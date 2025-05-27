package com.example.surveyapp.Repository;

import com.example.surveyapp.Entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResponseRepository extends JpaRepository<Response,Long> {
    List<Response> findBySurveyId(Long surveyId);
    List<Response> findByUserId(Long userId);
}
