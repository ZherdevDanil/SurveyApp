package com.example.surveyapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSurveyRequest {
        private Long id;
        private String title;
        private String description;

        private boolean requireAuth;
        private List<QuestionUpdateDto> questions;

}
