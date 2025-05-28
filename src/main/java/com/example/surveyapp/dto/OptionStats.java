package com.example.surveyapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionStats {
    private Long optionId;
    private String text;
    private Long count;
}
