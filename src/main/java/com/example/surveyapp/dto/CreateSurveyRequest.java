package com.example.surveyapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSurveyRequest {
    private Long id;
    private String title;
    private String description;

    private boolean requireAuth;
    @JsonProperty("isPublic")
    private boolean isPublic;
    private Instant activeFrom;
    private Instant activeUntil;
    /*private List<CreateQuestionRequest> questions;*/
    private List<QuestionUpdateDto> questions;
}
