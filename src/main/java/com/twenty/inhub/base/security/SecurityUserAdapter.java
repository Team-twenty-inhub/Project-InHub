package com.twenty.inhub.base.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

public interface SecurityUserAdapter extends UserDetails, OAuth2User {
    @Override
    default boolean isAccountNonExpired() {
        return true;
    }

    @Override
    default boolean isAccountNonLocked() {
        return true;
    }

    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    default boolean isEnabled() {
        return true;
    }

    @Override
    default Map<String, Object> getAttributes() {
        return new HashMap<>();
    }
}
