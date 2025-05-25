package com.example.surveyapp.Service;

import com.example.surveyapp.Entity.Question;
import com.example.surveyapp.Entity.Survey;
import com.example.surveyapp.Repository.QuestionRepository;
import com.example.surveyapp.Repository.SurveyRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    public final QuestionRepository questionRepository;
    public final SurveyRepository surveyRepository;

    public QuestionService(QuestionRepository questionRepository, SurveyRepository surveyRepository) {
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
    }

    public Question addQuestionToSurvey(Long surveyId, Question question){
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(()->new RuntimeException("Survey not found"));
        question.setSurvey(survey);
        return questionRepository.save(question);
    }
}
