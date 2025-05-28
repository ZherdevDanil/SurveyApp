package com.example.surveyapp.dto;

import com.example.surveyapp.Entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResultResponse {
    private Long questionId;
    private String text;
    private QuestionType questionType;
    private List<OptionStats> optionStats;
    private List<String> textAnswers;
}
