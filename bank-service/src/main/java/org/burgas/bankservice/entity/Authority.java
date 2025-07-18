package org.burgas.bankservice.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

    ADMIN,
    EMPLOYEE,
    USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
