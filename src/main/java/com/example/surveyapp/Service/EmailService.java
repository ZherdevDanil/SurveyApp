package com.example.surveyapp.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    private String activationBaseUrl = "http://localhost:4200";

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendActivationEmail(String to,String token){
        String link = activationBaseUrl + "/activate?token=" + token;
        String subject="Активація акаунту";
        String body =
                """
                Вітаємо.
                Перейдіть за наданим посиланням для активації акаунту, після цього ви можете пройти автентифікацію в системі.
                """+ link;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}
