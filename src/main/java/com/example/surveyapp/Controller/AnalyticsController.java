package com.example.surveyapp.Controller;

import com.example.surveyapp.Entity.Survey;
import com.example.surveyapp.Entity.User;
import com.example.surveyapp.Service.AnalyticsService;
import com.example.surveyapp.Service.SurveyService;
import com.example.surveyapp.dto.SurveyResultsResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    private final SurveyService surveyService;

    public AnalyticsController(AnalyticsService analyticsService, SurveyService surveyService) {
        this.analyticsService = analyticsService;
        this.surveyService = surveyService;
    }
    @GetMapping("/{surveyId}/results")
    public ResponseEntity<SurveyResultsResponse> getSurveyResults(@PathVariable Long surveyId, HttpServletRequest httpServletRequest) throws AccessDeniedException {
        Survey survey = surveyService.findById(surveyId).orElseThrow(()->new EntityNotFoundException("Survey not found"));
        String username = surveyService.extractUsername(httpServletRequest);
        if (!survey.getCreator().getUsername().equals(username)){
            throw new AccessDeniedException("You are not allowed to see result survey results");
        }
        SurveyResultsResponse response = analyticsService.getResultsForSurvey(surveyId,username);
        return ResponseEntity.ok(response);
    }

}
