package com.zmarket.userservice.util;

import com.zmarket.userservice.modules.security.enums.AuthProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

public class CustomAuthenticationToken implements Authentication {

    private final String id;

    private final AuthProvider authProvider;

    public CustomAuthenticationToken(String idToken, AuthProvider authProvider) {
        this.id = idToken;
        this.authProvider = authProvider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return new HashSet<SimpleGrantedAuthority>();
    }

    @Override
    public Object getCredentials() {
        return this.id;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }

}
