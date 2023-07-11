package com.twenty.inhub.base.filter;

import com.twenty.inhub.base.security.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DeviceAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().contains("/device") ||request.getRequestURI().contains("/common")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();

        if (!isAuthenticated) {
            filterChain.doFilter(request, response);
            return;
        }

        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();

        if (user.isDeviceAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        String deviceId = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("deviceId")) {
                    deviceId = cookie.getValue();
                    break;
                }
            }
        }

        String finalDeviceId = deviceId;
        boolean isPresent = user.getDeviceIds().stream()
                .anyMatch(id -> id.equals(finalDeviceId));

        if (isPresent) {
            user.deviceAuthenticationComplete();
            filterChain.doFilter(request, response);
            return;
        }

        response.sendRedirect("/device/authentication");
    }
}