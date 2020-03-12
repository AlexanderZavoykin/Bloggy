package com.gmail.aazavoykin.configuration;

import com.gmail.aazavoykin.configuration.properties.AppProperties;
import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.security.AppUserDetailsService;
import com.gmail.aazavoykin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.util.Optional;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final AppUserDetailsService userDetailsService;
    private final AppProperties appProperties;
    private final BloggyAuthenticationFailureHandler commonAuthenticationFailureHandler;
    private final BloggyAuthenticationSuccessHandler commonAuthenticationSuccessHandler;
    private final BloggyAuthenticationEntryPoint commonAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureAuthenticationListener() {
        return e -> {
            final String email = e.getAuthentication().getName();
            Optional.ofNullable(userService.getByEmail(email)).ifPresent(user -> {
                final Long last30MinAttemptsNum = userService.insertLoginAttempt(user.getId(), false);
                if (last30MinAttemptsNum > appProperties.getLogin().getTries()) {
                    userService.setEnabled(user, false);
                    // TODO add task to set enabled back to true after 30 minutes
                }
            });
        };
    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> successAuthenticationListener() {
        return e -> {
            final User user = userService.getByEmail(e.getAuthentication().getName());
            userService.insertLoginAttempt(user.getId(), true);
        };
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api-docs", "/configuration/ui", "/swagger-ui.html");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .formLogin().loginProcessingUrl("/login").usernameParameter("login").passwordParameter("password")
            .successHandler(commonAuthenticationSuccessHandler)
            .failureHandler(commonAuthenticationFailureHandler)
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(commonAuthenticationEntryPoint)
            .and()
            .authorizeRequests()
            .antMatchers("/user/password/**").permitAll()
            .anyRequest().authenticated()
            .requestMatchers(EndpointRequest.to("health", "metrics")).permitAll();
    }
}

