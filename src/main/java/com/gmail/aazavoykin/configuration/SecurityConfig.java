package com.gmail.aazavoykin.configuration;

import com.gmail.aazavoykin.db.model.enums.RoleName;
import com.gmail.aazavoykin.security.BloggyAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BloggyAuthenticationProvider authenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/swagger-ui/**", "/api-docs/**").hasAuthority(RoleName.ADMIN.getAuthority())
                .antMatchers("/login", "/sign_up").anonymous()
                .antMatchers("/user/**").authenticated()
               .antMatchers("/user/**").hasAuthority(RoleName.USER.getAuthority())
                .and().csrf().disable()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/process")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/login?error=true")
                .and()
                .exceptionHandling()
                .and()
                .logout()
                .logoutSuccessUrl("/");
    }

}
