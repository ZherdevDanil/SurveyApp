package com.example.surveyapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResultsResponse {
    private Long surveyId;
    private String title;
    private List<QuestionResultResponse> questionResultResponses;
}
