package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.configuration.properties.AppProperties;
import com.gmail.aazavoykin.configuration.properties.EmailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private final AppProperties appProperties;

    public void sendVerificationUrl(String email, String token) {
        final EmailProperties.MessageProperties activationProperties = emailProperties.getTemplate().getAuth().getActivation();
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(activationProperties.getSubject());
        message.setText(String.format(activationProperties.getBody(),
            appProperties.getHostname() + "/user/activate/" + token));
//        mailSender.send(message);
    }

    public void sendSuccessfulRegistrationConfirmation(String email) {
        final EmailProperties.MessageProperties confirmationProperties = emailProperties.getTemplate().getAuth().getConfirmation();
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(confirmationProperties.getSubject());
        message.setText(confirmationProperties.getBody());
//        mailSender.send(message);
    }

    public void sendPasswordResetUrl(String email, String token) {
        final EmailProperties.MessageProperties resetProperties = emailProperties.getTemplate().getAuth().getReset();
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(resetProperties.getSubject());
        message.setText(String.format(resetProperties.getBody(),
            appProperties.getHostname() + "/user/reset/" + token + "?email=" + email));
//        mailSender.send(message);
    }

    public void sendPasswordResetSuccessMessage(String email) {
    }
}
