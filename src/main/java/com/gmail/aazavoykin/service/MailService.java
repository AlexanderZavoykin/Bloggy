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

    public void sendActivationUrl(String to, String token) {
        final EmailProperties.TemplateProperties.AuthProperties.MessageProperties activationProperties =
            emailProperties.getTemplate().getAuth().getActivation();
        sendMessage(to, activationProperties.getSubject(), String.format(activationProperties.getBody(),
            appProperties.getHostname() + "/user/auth/activate/" + token));
    }

    public void sendSuccessfulRegistrationConfirmation(String to) {
        final EmailProperties.TemplateProperties.AuthProperties.MessageProperties confirmationProperties =
            emailProperties.getTemplate().getAuth().getRegistrationConfirmation();
        sendMessage(to, confirmationProperties.getSubject(), confirmationProperties.getBody());
    }

    public void sendPasswordResetUrl(String to, String token) {
        final EmailProperties.TemplateProperties.AuthProperties.MessageProperties resetProperties =
            emailProperties.getTemplate().getAuth().getResetPassword();
        sendMessage(to, resetProperties.getSubject(), String.format(resetProperties.getBody(),
            appProperties.getHostname() + "/user/password/change/" + token));
    }

    public void sendPasswordResetSuccessMessage(String to) {
        final EmailProperties.TemplateProperties.AuthProperties.MessageProperties successProperties =
            emailProperties.getTemplate().getAuth().getChangePasswordSuccess();
        sendMessage(to, successProperties.getSubject(), successProperties.getBody());
    }

    private void sendMessage(String to, String subject, String text) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
