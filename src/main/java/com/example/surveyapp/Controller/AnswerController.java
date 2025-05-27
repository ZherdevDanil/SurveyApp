package com.example.surveyapp.Controller;

import com.example.surveyapp.Entity.Answer;
import com.example.surveyapp.Security.JwtService;
import com.example.surveyapp.Service.AnswerService;
import com.example.surveyapp.Service.SurveyService;
import com.example.surveyapp.dto.AnswerRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerService answerService;
    private final JwtService jwtService;

    private final SurveyService surveyService;

    public AnswerController(AnswerService answerService, JwtService jwtService, SurveyService surveyService) {
        this.answerService = answerService;
        this.jwtService = jwtService;
        this.surveyService = surveyService;
    }
    @PostMapping
    public ResponseEntity<?> submitAnswer(@RequestBody List<AnswerRequest> answerRequests, HttpServletRequest httpServletRequest) throws AccessDeniedException {
        String username = null;
        if (httpServletRequest.getHeader("Authorization")!=null && httpServletRequest.getHeader("Authorization").startsWith("Bearer ")){
            username = surveyService.extractUsername(httpServletRequest);
        }
        List<Answer> answer = answerService.sibmitAnswers(answerRequests,username);
        return ResponseEntity.ok(answer);
    }
}
