package com.example.surveyapp.dto;

import com.example.surveyapp.Entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionUpdateDto {
    private Long id;
    private String text;
    private QuestionType type;
    private Integer position;
    private List<CreateOptionRequest> options;
}
