package com.twenty.inhub.base.security;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.device.Device;
import com.twenty.inhub.boundedContext.device.DeviceRepository;
import com.twenty.inhub.boundedContext.device.DeviceService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final DeviceService deviceService;
    private final DeviceRepository deviceRepository;
    private final HttpServletResponse response;

    // 소셜 로그인이 성공할 때 마다 이 함수가 실행된다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthId = oAuth2User.getName();

        OAuthAttributes attributes = OAuthAttributes.of(providerTypeCode, oAuth2User.getAttributes());

        String username = providerTypeCode + "__%s".formatted(oauthId);

        RsData<Member> rsData = memberService.whenSocialLogin(providerTypeCode, username, attributes.getPicture(), attributes.getNickname(), attributes.getEmail());

        boolean isFirst = rsData.getResultCode().equals("S-1");

        Member member = rsData.getData();

        if(rsData.getResultCode().equals("S-1") && member.getEmail() != null && !member.getEmail().isBlank()) {
            String deviceId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("deviceId", deviceId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            deviceService.createAndSave(deviceId, rsData.getData());
        }

        List<String> userAgents = deviceRepository.findByMemberUsername(member.getUsername())
                .stream()
                .map(Device::getInfo)
                .toList();

        return new CustomOAuth2User(member.getId(), member.getUsername(), member.getPassword(), member.getEmail(), member.getGrantedAuthorities(), userAgents, false, isFirst);
    }
}