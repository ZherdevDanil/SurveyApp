package com.example.surveyapp.Entity;

import com.example.surveyapp.util.StringEncryptConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Survey {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Convert(converter = StringEncryptConverter.class)
    @Column(nullable = false, length = 1000)
    private String title;
    @Column(columnDefinition = "TEXT")
    //@Convert(converter = StringEncryptConverter.class)
    private String description;
    private boolean requireAuth;
    private boolean isActive;
    private boolean isPublic;

    private Instant activeFrom;
    private Instant activeUntil;


    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

}
