package com.twenty.inhub.base.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class CustomOAuth2User implements SecurityUserAdapter {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private List<String> deviceIds;
    private boolean isDeviceAuthenticated;
    private boolean isFirst;

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