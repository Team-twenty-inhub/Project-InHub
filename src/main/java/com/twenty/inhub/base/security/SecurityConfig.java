package com.twenty.inhub.base.security;

import com.twenty.inhub.base.filter.DeviceAuthenticationFilter;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.ut.ut.Ut;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final FailureHandler failureHandler;
    private final SuccessHandler successHandler;
    private final DeviceAuthenticationFilter deviceAuthenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable);
        return http
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login")
                )
                .formLogin(
                        loginFail -> loginFail
                                .failureHandler(failureHandler)
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/member/login")
                                .successHandler(successHandler)
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/member/logout")
                                .logoutSuccessUrl("/member/login")
                )
                .addFilterBefore(deviceAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

@Component
class FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String redirectUrl = "/member/login";

        String msg = Ut.url.encode("아이디 또는 비밀번호가 일치하지 않습니다.");

        response.sendRedirect(redirectUrl + "?error=true&msg=" + msg);
    }
}

@Component
@RequiredArgsConstructor
@Slf4j
class SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String firstRedirectUrl = "/member/profileUpdate";
        String visitedRedirectUrl = "/";

        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();

        if(principal.isFirst()) {
            log.info("첫 소셜 로그인 회원 = {}", principal.getUsername());
            response.sendRedirect(firstRedirectUrl);
            return;
        }

        response.sendRedirect(visitedRedirectUrl);
    }
}