package com.example.surveyapp.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verif_token")
public class VerificationToken {
    @Id
    @GeneratedValue
    private Long id;
    @Convert(disableConversion = true)
    private String token;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Instant exiryDate;
}
