package com.gmail.aazavoykin.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties("app")
public class AppProperties {

    private final AuthProperties auth;
    private final String hostname;

    @Data
    public static class AuthProperties {

        private final SigninProperties signin;
        private final SignupProperties signup;
        private final ForgotProperties forgot;
    }

    @Data
    public static class SigninProperties {

        private final int tries;
        private final long breaktime;
    }

    @Data
    public static class SignupProperties {

        private final int lifetime;
    }

    @Data
    public static class ForgotProperties {

        private final int lifetime;
    }
}
