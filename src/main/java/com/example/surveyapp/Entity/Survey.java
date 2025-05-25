package com.example.surveyapp.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Survey {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean isAnonymous; // можна пройти без логіну
    private boolean requireAuth; // обов’язкова авторизація
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

}
