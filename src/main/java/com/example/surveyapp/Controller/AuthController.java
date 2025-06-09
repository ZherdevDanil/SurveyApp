package com.example.surveyapp.Controller;

import com.example.surveyapp.Entity.User;
import com.example.surveyapp.Entity.VerificationToken;
import com.example.surveyapp.Repository.UserRepository;
import com.example.surveyapp.Repository.VerificationTokenRepository;
import com.example.surveyapp.Security.AuthenticationService;
import com.example.surveyapp.Security.RecaptchaService;
import com.example.surveyapp.dto.AuthenticationRequest;
import com.example.surveyapp.dto.AuthenticationResponse;
import com.example.surveyapp.dto.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final RecaptchaService recaptchaService;

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    public AuthController(AuthenticationService authenticationService, RecaptchaService recaptchaService, VerificationTokenRepository verificationTokenRepository, UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.recaptchaService = recaptchaService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
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
        User user = userRepository.findByUsername(request.getUsername()).get();
        if (!user.isEnabled()){
            System.out.println("Акаунт користувача не активован");
            throw new DisabledException("Потрібна активація через пошту");

        }else {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        }


    }
    @GetMapping("/activate")
    public ResponseEntity<String> activate(@RequestParam String token){
        var verifToken = verificationTokenRepository.findByToken(token);
        if (verifToken.isEmpty()){
            return ResponseEntity.badRequest().body("Невірний токен");
        }
        VerificationToken vtok = verifToken.get();
        if (vtok.getExiryDate().isBefore(Instant.now())){
            return ResponseEntity.badRequest().body("Термін дії токена сплив");
        }
        User user = vtok.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok("Акаунт успішно активовано");

    }
}
