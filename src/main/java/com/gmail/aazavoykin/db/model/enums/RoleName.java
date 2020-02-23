package com.gmail.aazavoykin.db.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum RoleName implements GrantedAuthority {

    USER,

    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

}
