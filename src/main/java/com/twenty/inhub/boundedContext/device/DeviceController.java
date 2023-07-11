package com.twenty.inhub.boundedContext.device;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final Rq rq;

    @GetMapping("/authentication")
    public String showAuthentication() {
        return "usr/device/authentication";
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/authentication")
//    public String preAuthenticationForm() {
//        Member member = rq.getMember();
//
//        RsData<String> rsData = deviceService.createCode(member);
//
//        rq.getSession().setAttribute("code", rsData.getData());
//
//        log.info("기기 인증코드 = {}", rsData.getData());
//
//        return "usr/member/device/authentication";
//    }
//
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/authentication")
//    public String authentication(String inputCode, HttpServletRequest request) {
//        String code = (String) rq.getSession().getAttribute("code");
//        log.info("code = {}", code);
//        log.info("inputCode = {}", inputCode);
//
//        if(!inputCode.equals(code)) {
//            return rq.historyBack("기기 인증 실패<br>인증코드를 재발송했습니다.");
//        }
//
//        String userAgent = request.getHeader("User-Agent");
//
//        RsData<Device> rsData = deviceService.addAuthenticationDevice(rq.getMember(), userAgent);
//
//        return rq.redirectWithMsg("/", rsData);
//    }
}
