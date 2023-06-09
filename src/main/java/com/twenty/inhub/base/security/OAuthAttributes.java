package com.twenty.inhub.base.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@ToString
@Slf4j
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nickname;
    private String picture;
    private String email;

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes){
        // 여기서 깃허브와 카카오, 구글 구분 (ofGitHub, ofKakao. ofGoogle)
        if("GITHUB".equals(registrationId)) {
            return ofGitHub(attributes);
        }

        if("KAKAO".equals(registrationId)) {
            return ofKakao(attributes);
        }

        return ofGoogle(attributes);
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        log.info("구글 수집 정보 = {}", attributes);

        return OAuthAttributes.builder()
                .nickname((String) attributes.get("sub"))
                .picture((String) attributes.get("picture"))
                .email((String) attributes.get("email"))
                .build();
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        log.info("카카오 수집 정보 = {}", kakaoAccount);

        return OAuthAttributes.builder()
                .nickname(attributes.get("id").toString())
                .picture((String) kakaoProfile.get("profile_image_url"))
                .email((String) kakaoAccount.get("email"))
                .build();
    }

    private static OAuthAttributes ofGitHub(Map<String, Object> attributes) {
        log.info("깃허브 수집 정보 = {}", attributes);

        return OAuthAttributes.builder()
                .nickname((String) attributes.get("login"))
                .picture((String) attributes.get("avatar_url"))
                .build();
    }

}
