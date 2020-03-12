package com.gmail.aazavoykin.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties("app")
public class AppProperties {

    private final TokenProperties token;
    private final LoginProperties login;
    private final String hostname;

    @Data
    public static class TokenProperties {

        private final String header;
        private final String prefix;
        private final String secret;
        private final long expiration;
    }

    @Data
    public static class LoginProperties {

        private final int tries;
        private final long breaktime;
    }
}
