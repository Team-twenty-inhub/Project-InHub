package com.twenty.inhub.base.interceptor;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.security.CustomOAuth2User;
import com.twenty.inhub.boundedContext.device.Device;
import com.twenty.inhub.boundedContext.device.DeviceRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeviceCheckInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final DeviceRepository deviceRepository;
    private final Rq rq;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(rq.isLogout()) {
            log.info("인터셉터 -> 로그아웃 상태");
        }

        if(rq.isLogin()) {
            CustomOAuth2User user = (CustomOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Member member = memberService.findByUsername(user.getUsername()).orElseThrow();

            log.info("인터셉터 -> 로그인 상태({})", member);

            String username = member.getUsername();
            if(username.equals("admin") || username.equals("user1")) {
                return true;
            }

            List<Device> devices = deviceRepository.findByMemberUsername(member.getUsername());

            String userAgent = request.getHeader("User-Agent");
            log.info("현재 접속 기기 = {}", userAgent);

            boolean isDeviceAuthentication = devices.stream()
                    .anyMatch(device -> device.getInfo().equals(userAgent));

            if(!isDeviceAuthentication) {
                response.sendRedirect("/device/authentication");
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
