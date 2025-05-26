package com.example.surveyapp.dto;

import com.example.surveyapp.Entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {
    private String text;
    private QuestionType type;
    private Integer position;
    private List<String> options;
}
