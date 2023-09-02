package com.dizertatie.backend.user.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final com.dizertatie.backend.user.model.User user;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, com.dizertatie.backend.user.model.User user) {
        super(username, password, authorities);
        this.user = user;
    }

    public com.dizertatie.backend.user.model.User getUser() {
        return user;
    }

}
