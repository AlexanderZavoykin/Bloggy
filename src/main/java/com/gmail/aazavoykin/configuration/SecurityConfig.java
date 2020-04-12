package com.gmail.aazavoykin.configuration;

import com.gmail.aazavoykin.configuration.properties.AppProperties;
import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.security.AppUser;
import com.gmail.aazavoykin.security.AppUserDetailsService;
import com.gmail.aazavoykin.security.AppAuthenticationEntryPoint;
import com.gmail.aazavoykin.security.AppAuthenticationFailureHandler;
import com.gmail.aazavoykin.security.AppAuthenticationSuccessHandler;
import com.gmail.aazavoykin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AppUserDetailsService userDetailsService;
    private final AppProperties appProperties;
    private final AppAuthenticationFailureHandler authenticationFailureHandler;
    private final AppAuthenticationSuccessHandler authenticationSuccessHandler;
    private final AppAuthenticationEntryPoint authenticationEntryPoint;
    private final ThreadPoolTaskScheduler scheduler;

    @Bean
    public ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureAuthenticationListener() {
        return e -> {
            final String email = e.getAuthentication().getName();
            log.debug("Got auth try with email {}", email);
            Optional.ofNullable((AppUser) userDetailsService.loadUserByUsername(email)).ifPresent(user -> {
                final Long last30MinAttemptsNum = userService.insertLoginAttempt(user.getId(), false);
                log.debug("Counted {} login failures for last 30 minutes for email {}", last30MinAttemptsNum, email);
                if (last30MinAttemptsNum > appProperties.getAuth().getSignin().getTries()) {
                    userService.setEnabled(user.getId(), false);
                    scheduler.schedule(() -> userService.setEnabled(user.getId(), true),
                        new Date(System.currentTimeMillis() + appProperties.getAuth().getSignin().getBreaktime() * 60 * 1000));
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
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-ui.html");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .formLogin().loginProcessingUrl("/login").usernameParameter("login").passwordParameter("password")
            .successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler)
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .authorizeRequests()
            .antMatchers("/login", "/users/auth/**", "/users/password/**").anonymous()
            .antMatchers("/stories", "/stories/last10", "/stories/{id}", "/tags/all",
                "/users", "/users/{nickname}/**").permitAll();
    }
}

