package com.gmail.aazavoykin.configuration;

import com.gmail.aazavoykin.configuration.properties.EmailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@RequiredArgsConstructor
@Configuration
public class EmailConfig {

    private final EmailProperties emailProperties;

    @Bean
    public JavaMailSender mailSender() {
        final EmailProperties.HostPortProperties smtpProperties = emailProperties.getSmtp();
        final JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(smtpProperties.getHost());
        sender.setPort(smtpProperties.getPort());
        sender.setUsername(emailProperties.getUsername());
        sender.setPassword(emailProperties.getPassword());
        return sender;
    }

}
