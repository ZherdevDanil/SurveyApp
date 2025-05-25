package com.example.surveyapp.Controller;

import com.example.surveyapp.Entity.Survey;
import com.example.surveyapp.Service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class QuestionController {
    private final SurveyService surveyService;


    public QuestionController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey){
        Survey created = surveyService.createSurvey(survey);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id){
        return surveyService.findById(id).map(survey -> new ResponseEntity<>(survey, HttpStatus.OK)).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Survey>> getAllSurveys(Long id) {
        return new ResponseEntity<>(surveyService.getSurveys(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
