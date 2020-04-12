package com.gmail.aazavoykin.security;

import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.getByEmailIgnoreCase(username);
        return new AppUser(user.getId(), user.getEmail(), user.getPassword(), user.isEnabled(), user.getRoles());
    }
}
