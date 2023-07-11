package com.twenty.inhub.boundedContext.device;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.base.security.CustomOAuth2User;
import com.twenty.inhub.boundedContext.mail.service.MailService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final MailService mailService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/authentication")
    public String showAuthentication() {
        return "usr/device/authentication";
    }

    @GetMapping("/check")
    public String showCheck(@AuthenticationPrincipal CustomOAuth2User user) {
        if(user.isDeviceAuthenticated()) {
            return "redirect:/";
        }

        RsData<String> rsData = deviceService.createCode(rq.getMember());

        log.info("회원(id = {}) 기기 체크 코드 = {}", user.getId(), rsData.getData());

        rq.getSession().setAttribute("device-check-code", rsData.getData());

        return "usr/device/check";
    }

    @PostMapping("/check")
    public String check(
            @AuthenticationPrincipal CustomOAuth2User user,
            String code,
            HttpServletResponse response
    ) {
        if (user.isDeviceAuthenticated()) {
            return "redirect:/";
        }

        log.info("회원(id = {}) 입력한 기기 체크 코드 = {}", user.getId(), code);

        String deviceCheckUUID = (String) rq.getSession().getAttribute("device-check-code");

        if (deviceCheckUUID.isBlank() || !deviceCheckUUID.equals(code)) {
            return rq.redirectWithMsg("/device/authentication", "보안을 위해 현재 사용중인 기기를 등록해주세요.");
        }

        String deviceId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("deviceId", deviceId);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*365);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        Optional<Member> opMember = memberService.findById(user.getId());
        if(opMember.isEmpty()) {
            return rq.historyBack("존재하지 않는 회원입니다.");
        }

        RsData<Device> rsData = deviceService.createAndSave(deviceId, opMember.get());

        user.deviceAuthenticationComplete();

        return rq.redirectWithMsg("/", rsData.getMsg());
    }
}
