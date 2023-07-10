package com.twenty.inhub.boundedContext.developer;

import com.twenty.inhub.base.jwt.JwtProvider;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final JwtProvider jwtProvider;


    //-- jwt 발급 --//
    public String create(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());

        return jwtProvider.getToken(claims, 366 * 24 * 60 * 60);
    }
}
