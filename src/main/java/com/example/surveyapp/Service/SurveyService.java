package com.example.surveyapp.Service;

import com.example.surveyapp.Entity.Survey;
import com.example.surveyapp.Repository.SurveyRepository;
import com.example.surveyapp.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {
    public final SurveyRepository surveyRepository;
    public final UserRepository userRepository;

    public SurveyService(SurveyRepository surveyRepository, UserRepository userRepository){
        this.surveyRepository=surveyRepository;
        this.userRepository = userRepository;
    }

    public List<Survey> getSurveys(Long id){
        List<Survey> surveys = surveyRepository.findByCreatorId(id);
        return surveys;
    }

    public Survey createSurvey(Survey survey){
        return surveyRepository.save(survey);

    }

    public Optional<Survey> findById(Long id){
        return surveyRepository.findById(id);
    }

    public void deleteSurvey(Long id){
        surveyRepository.deleteById(id);
    }
}
