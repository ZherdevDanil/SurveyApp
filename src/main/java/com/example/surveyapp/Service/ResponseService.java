package com.example.surveyapp.Service;

import com.example.surveyapp.Entity.Response;
import com.example.surveyapp.Repository.ResponseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ResponseService {
    public final ResponseRepository responseRepository;

    public ResponseService(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }


    public Response saveResponse(Response response){
        return responseRepository.save(response);
    }

    public List<Response> getResponsesBySurvey(Long surveyId){
        return responseRepository.findBySurveyId(surveyId);
    }
}
