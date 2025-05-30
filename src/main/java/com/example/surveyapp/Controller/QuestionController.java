package com.example.surveyapp.Controller;

import com.example.surveyapp.Entity.Question;
import com.example.surveyapp.Entity.Survey;
import com.example.surveyapp.Service.QuestionService;
import com.example.surveyapp.Service.SurveyService;
import com.example.surveyapp.dto.CreateQuestionRequest;
import com.example.surveyapp.dto.QuestionUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class QuestionController {
    private final QuestionService questionService;

    private final SurveyService surveyService
            ;

    public QuestionController(QuestionService questionService, SurveyService surveyService) {
        this.questionService = questionService;
        this.surveyService = surveyService;
    }

    @PostMapping("/surveys/{surveyId}/questions")
    public ResponseEntity<Question> createQuestion(@PathVariable Long surveyId, @RequestBody QuestionUpdateDto questionUpdateDto/*CreateQuestionRequest*/, HttpServletRequest httpServletRequest) throws AccessDeniedException {
        String username = surveyService.extractUsername(httpServletRequest);
        Question created = questionService.createQuestion(surveyId,questionUpdateDto,username);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/surveys/{surveyId}/questions")
    public ResponseEntity<List<Question>> getQuestionsBySurvey(@PathVariable Long surveyId,HttpServletRequest httpServletRequest) throws AccessDeniedException {
        String username = surveyService.extractUsername(httpServletRequest);
        List<Question> questions = questionService.getQuestionsBySurvey(surveyId,username);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("/questions/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long questionId, @RequestBody QuestionUpdateDto questionUpdateDto/*CreateQuestionRequest createQuestionRequest*/, HttpServletRequest httpServletRequest) throws AccessDeniedException {
        try{
            String username = surveyService.extractUsername(httpServletRequest);
            Question updatedQuestion = questionService.updateQuestion(questionId,questionUpdateDto,username);
            return ResponseEntity.ok(updatedQuestion);}
        catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",ex.getMessage()));
        }
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId,HttpServletRequest httpServletRequest) throws AccessDeniedException {
        String username = surveyService.extractUsername(httpServletRequest);
        questionService.deleteQuestion(questionId,username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
