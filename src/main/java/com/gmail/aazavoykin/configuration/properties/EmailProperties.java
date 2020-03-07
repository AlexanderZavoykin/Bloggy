package com.gmail.aazavoykin.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties("email")
public class EmailProperties {

    private final HostPortProperties smtp;
    private final TemplateProperties template;
    private final String username;
    private final String password;

    @Data
    public static class HostPortProperties {

        private final String host;
        private final int port;
    }

    @Data
    public static class TemplateProperties {

        private final AuthProperties auth;
    }

    @Data
    public static class AuthProperties {

        private final MessageProperties activation;
        private final MessageProperties confirmation;
        private final MessageProperties reset;
    }

    @Data
    public static class MessageProperties {

        private final String subject;
        private final String body;
    }
}
