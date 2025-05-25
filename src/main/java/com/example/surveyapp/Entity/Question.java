package com.example.surveyapp.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType; // SINGLE_CHOICE, MULTIPLE_CHOICE, TEXT

    private Integer order;
}
