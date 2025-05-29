package com.example.surveyapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicSurveysResponse {
    private Long id;
    private String title;
    private String description;
}
