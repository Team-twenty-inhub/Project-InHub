package com.twenty.inhub.base.security;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final Rq rq;

    // 카카오톡 로그인이 성공할 때 마다 이 함수가 실행된다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthId = switch (providerTypeCode) {
            case "NAVER" -> ((Map<String, String>) oAuth2User.getAttributes().get("response")).get("id");
            default -> oAuth2User.getName();
        };

        String profileImg = "";

        if(providerTypeCode.equals("GITHUB")) {
            // 깃허브 API 호출을 위한 사용자 정보 가져오기
            String accessToken = userRequest.getAccessToken().getTokenValue();
            String apiUrl = "https://api.github.com/user";

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().setBearerAuth(accessToken);
                return execution.execute(request, body);
            });

            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
            Map<String, String> responseData = response.getBody();

            profileImg = responseData.get("avatar_url");
            System.out.println(profileImg);
        }

        if(providerTypeCode.equals("KAKAO")) {
            Map<String, Object> map = oAuth2User.getAttributes();
            System.out.println(map);
            Map<String, String> properties = (Map<String, String>) map.get("properties");
            System.out.println(properties);
            profileImg = properties.get("profile_image");
            System.out.println(profileImg);
        }

        if (providerTypeCode.equals("GOOGLE")) {
            Map<String, Object> map = oAuth2User.getAttributes();
            System.out.println(map);
            String pictureUrl = (String) map.get("picture");
            System.out.println(pictureUrl);
            profileImg = pictureUrl;
        }

        String username = providerTypeCode + "__%s".formatted(oauthId);

        Member member = memberService.whenSocialLogin(providerTypeCode, username, profileImg).getData();

        return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
    }
}

class CustomOAuth2User extends User implements OAuth2User {

    public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}