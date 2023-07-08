package com.twenty.inhub.base.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtProviderTest {

    @Autowired JwtProvider jwtProvider;

    @Value("${custom.jwt.secret-key}")
    private String secretKeyPlan;

    @Test
    @DisplayName("비밀키 존재 확인")
    void no1() {
        assertThat(secretKeyPlan).isNotNull();
    }

    @Test
    @DisplayName("비밀키 hmac 암호화")
    void no2() {
        SecretKey secretKey = jwtProvider.getSecretKey();
        assertThat(secretKey).isNotNull();
    }

    @Test
    @DisplayName("access token 생성")
    void no3() {

        Map<String, Object> claim = new HashMap<>();
        claim.put("id", 1L);

        String accessToken = jwtProvider.getToken(claim, 60 * 60 ^ 5);
        System.out.println("ACCESS TOKEN : " + accessToken);

        assertThat(accessToken).isNotNull();
    }
}