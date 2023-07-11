package com.twenty.inhub.base.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
public class CustomOAuth2User implements SecurityUserAdapter {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private List<String> deviceIds;
    private boolean isDeviceAuthenticated;

    public CustomOAuth2User(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.id = id;
    }

    public void deviceAuthenticationComplete() {
        this.isDeviceAuthenticated = true;
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}