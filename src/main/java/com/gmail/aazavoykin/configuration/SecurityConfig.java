package com.gmail.aazavoykin.configuration;

import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.db.model.enums.Role;
import com.gmail.aazavoykin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

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
                if (last30MinAttemptsNum > 5) {
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
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/swagger-ui/**", "/api-docs/**").hasAuthority(Role.ADMIN.getAuthority())
            .antMatchers("/login", "/sign_up").permitAll()
            .antMatchers("/user/**").permitAll()//.authenticated()
            .antMatchers("/user/**").permitAll()//.hasAuthority(Role.USER.getAuthority())
            .anyRequest().authenticated()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().csrf().disable()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/user/login")
            .usernameParameter("email")
            .passwordParameter("password")
            .failureUrl("/login?error=true")
            .and().exceptionHandling()
            .and().logout().permitAll()
            .logoutSuccessUrl("/");
    }
}
