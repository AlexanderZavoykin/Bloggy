package com.gmail.aazavoykin.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Optional;

public class AppUser extends User {

    private final Long id;

    public AppUser(Long id, String username, String password, boolean enabled,
                   Collection<? extends GrantedAuthority> authorities) {

        super(username, password, enabled, true, true, true, authorities);
        this.id = id;
    }

    public static Optional<AppUser> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .filter(Authentication::isAuthenticated)
            .filter(a -> a.getPrincipal() instanceof AppUser)
            .map(a -> (AppUser) a.getPrincipal());
    }

    public Long getId() {
        return id;
    }

    public <R extends Enum> boolean hasRole(final R e) {
        return getAuthorities().contains(new SimpleGrantedAuthority(e.name()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AppUser appUser = (AppUser) o;
        return id.equals(appUser.id);
    }
}
