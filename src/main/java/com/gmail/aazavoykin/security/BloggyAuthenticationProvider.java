package com.gmail.aazavoykin.security;

import com.gmail.aazavoykin.exception.EntityNotFoundException;
import com.gmail.aazavoykin.model.Role;
import com.gmail.aazavoykin.model.User;
import com.gmail.aazavoykin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BloggyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw  new EntityNotFoundException("User with username='" + username + "' is not found");
        }
        final String password = authentication.getCredentials().toString();
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("That email and password combination is incorrect.");
        }
        final List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.USER.name());
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authClass) {
        return authClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}
