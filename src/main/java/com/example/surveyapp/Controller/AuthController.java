package com.example.surveyapp.Controller;

import com.example.surveyapp.Security.AuthenticationService;
import com.example.surveyapp.Security.RecaptchaService;
import com.example.surveyapp.dto.AuthenticationRequest;
import com.example.surveyapp.dto.AuthenticationResponse;
import com.example.surveyapp.dto.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final RecaptchaService recaptchaService;

    public AuthController(AuthenticationService authenticationService, RecaptchaService recaptchaService) {
        this.authenticationService = authenticationService;
        this.recaptchaService = recaptchaService;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (!recaptchaService.verify(request.getRecaptchaToken())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Невірна reCAPTCHA"));
        }
        authenticationService.register(request);
        return ResponseEntity.ok(Map.of("message","Перевірте пошту"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
