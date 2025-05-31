package com.example.surveyapp.Controller;

import com.example.surveyapp.Entity.Survey;
import com.example.surveyapp.Entity.User;
import com.example.surveyapp.Repository.UserRepository;
import com.example.surveyapp.Security.JwtService;
import com.example.surveyapp.Service.SurveyService;
import com.example.surveyapp.dto.CreateSurveyRequest;
import com.example.surveyapp.dto.PublicSurveysResponse;
import com.example.surveyapp.dto.UpdateSurveyRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.AcceptPendingException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {
    private final SurveyService surveyService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    public SurveyController(SurveyService surveyService, UserRepository userRepository, JwtService jwtService) {
        this.surveyService = surveyService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody CreateSurveyRequest createSurveyRequest, HttpServletRequest httpServletRequest) {
        String username = surveyService.extractUsername(httpServletRequest);
        if (username == null ) return ResponseEntity.status(401).build();
        Survey createdSurvey = surveyService.createSurvey(createSurveyRequest,username);
        return ResponseEntity.ok(createdSurvey);
    }
    @GetMapping("/public")
    public ResponseEntity<List<PublicSurveysResponse>> getAllPublicSurveys(){
        List<PublicSurveysResponse> publicSurveysResponse = surveyService.getAllPublicSurveys();
        return ResponseEntity.ok(publicSurveysResponse);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UpdateSurveyRequest> getSurveyById(@PathVariable Long id) throws AccessDeniedException {
        Survey survey = surveyService.findById(id).get();
        /*if(!survey.isActive() || (survey.getActiveFrom() != null && Instant.now().isBefore(survey.getActiveFrom()))
                || (survey.getActiveUntil()!=null && Instant.now().isAfter(survey.getActiveUntil()))){
            throw new AccessDeniedException("Опитування не активне");
        }*/

        return ResponseEntity.ok(surveyService.getSurveyDetails(id));
    }

    @GetMapping("/survey-details/{id}")
    public ResponseEntity<UpdateSurveyRequest> getSurveyDetailsById(@PathVariable Long id) throws AccessDeniedException {
        return ResponseEntity.ok(surveyService.getSurveyDetails(id));
    }

    @GetMapping
    public ResponseEntity<List<Survey>> getAllUserSurveys(HttpServletRequest httpServletRequest) {
        String username = surveyService.extractUsername(httpServletRequest);
        if (username == null) return ResponseEntity.status(401).build();

        User user = userRepository.findByUsername(username).orElseThrow();
        List<Survey> surveys = surveyService.findByUser(user);
        return ResponseEntity.ok(surveys);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id, HttpServletRequest httpServletRequest) throws AccessDeniedException {
        String username = surveyService.extractUsername(httpServletRequest);
        if (username == null) return ResponseEntity.status(401).build();

        Survey survey = surveyService.findById(id).orElseThrow();

        if (!survey.getCreator().getUsername().equals(username)){
            throw new AccessDeniedException("You can delete only your own surveys");

        }

        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Survey> updateSurvey(@PathVariable Long id, @RequestBody Survey updatedSurvey, HttpServletRequest httpServletRequest ) throws AccessDeniedException {
        String username = surveyService.extractUsername(httpServletRequest);
        if (username == null) return ResponseEntity.status(401).build();

        Survey survey = surveyService.findById(id).orElseThrow();
        if (!survey.getCreator().getUsername().equals(username)){
            throw new AccessDeniedException("You can update only your surveys");
        }
        survey.setTitle(updatedSurvey.getTitle());
        survey.setDescription(updatedSurvey.getDescription());
        survey.setRequireAuth(updatedSurvey.isRequireAuth());
        survey.setPublic(updatedSurvey.isPublic());

        return ResponseEntity.ok(surveyService.save(survey));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<Survey> finish(@PathVariable Long id , HttpServletRequest httpServletRequest) throws AccessDeniedException {
        String username = surveyService.extractUsername(httpServletRequest);
        Survey finishedSurvey = surveyService.finishSurvey(id,username);
        return ResponseEntity.ok(finishedSurvey);
    }

}

