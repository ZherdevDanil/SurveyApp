package com.example.surveyapp.Controller;

import com.example.surveyapp.Entity.Response;
import com.example.surveyapp.Service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {
    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }


    public ResponseEntity<Response> submitResponse(@RequestBody Response response){
        Response saved = responseService.saveResponse(response);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    public ResponseEntity<List<Response>> getResponsesBySurvey(@PathVariable Long surveyId){
        return new ResponseEntity<>(responseService.getResponsesBySurvey(surveyId),HttpStatus.OK);
    }
}
