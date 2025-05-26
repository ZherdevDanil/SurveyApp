package com.example.surveyapp.Service;

import com.example.surveyapp.Entity.Survey;
import com.example.surveyapp.Entity.User;
import com.example.surveyapp.Repository.SurveyRepository;
import com.example.surveyapp.Repository.UserRepository;
import com.example.surveyapp.Security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {
    public final SurveyRepository surveyRepository;
    public final UserRepository userRepository;

    public final JwtService jwtService;

    public SurveyService(SurveyRepository surveyRepository, UserRepository userRepository, JwtService jwtService){
        this.surveyRepository=surveyRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
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


    public List<Survey> findByUser(User user){
        List<Survey> surveys = surveyRepository.findByCreatorId(user.getId());
        return surveys;
    }

    public void deleteSurvey(Long id){
        surveyRepository.deleteById(id);
    }

    public Survey save(Survey survey){
        return surveyRepository.save(survey);
    }


    public String extractUsername(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return null;
        }
        String jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }
}
