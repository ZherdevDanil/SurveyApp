package com.example.surveyapp.dto;

import lombok.Data;

import java.util.List;
@Data
public class AnswerRequest {
    private Long questionId;
    private String textAnswer;
    private List<Long> selecretOptionId;
}
