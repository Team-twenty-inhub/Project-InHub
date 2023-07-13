package com.twenty.inhub.base.jwt;

import com.twenty.inhub.ut.ut.Ut;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * SECRET KEY 조회 METHOD
 */
@Component
public class JwtProvider {

    private SecretKey cachedSecretKey;

    @Value("${custom.jwt.secret-key}")
    private String secretKeyPlain;

    private SecretKey _getSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    //-- secret key 조회 --//
    public SecretKey getSecretKey() {
        if (cachedSecretKey == null)
            cachedSecretKey = _getSecretKey();

        return cachedSecretKey;
    }

    //-- token 생성 --//
    public String getToken(Map<String, Object> claims, int seconds) {
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L + seconds);

        return Jwts.builder()
                .claim("body", Ut.json.toString(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    //-- token 유효성 검사 --//
    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token.replace("bearer", ""));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //-- token decoding --//
    public Map<String, Object> getClaims(String token) {
        String body = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("body", String.class);

        return Ut.json.toMap(body);
    }
}
