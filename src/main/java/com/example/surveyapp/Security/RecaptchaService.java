package com.example.surveyapp.Security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class RecaptchaService {
    @Value("${recaptcha.secret}")
    private String secret;

    private static final String VERIFY_URL="https://www.google.com/recaptcha/api/siteverify";

    private final RestTemplate rest = new RestTemplate();

    public boolean verify(String token){
        URI verifyUri = UriComponentsBuilder
                .fromHttpUrl(VERIFY_URL)
                .queryParam("secret",secret)
                .queryParam("response",token)
                .build()
                .toUri();

        var response = rest.postForObject(verifyUri,null,RecaptchaResponse.class);
        return response!=null && response.isSuccess();
    }
    @Data
    public static class RecaptchaResponse{
        private boolean success;
        private List<String> errorCodes;
    }
}
