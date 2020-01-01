package com.gmail.aazavoykin.security;

import com.gmail.aazavoykin.model.Role;
import com.gmail.aazavoykin.model.User;
import com.gmail.aazavoykin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleInfoNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BloggyUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username '" + username + "' is not found");
        }
        List<GrantedAuthority> authorities;
        Role role = user.getRole();
        if (role == null) {
            throw new RoleInfoNotFoundException("Inner error: cannot find such role");
        }
        authorities = AuthorityUtils.createAuthorityList(role.name());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                authorities);
        return userDetails;
    }

}
