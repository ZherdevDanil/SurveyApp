package com.example.surveyapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        @JsonProperty("isActive")
        private boolean isActive;

        private boolean requireAuth;
        private List<QuestionUpdateDto> questions;

}
