package com.twenty.inhub.base.interceptor;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.security.CustomOAuth2User;
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

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeviceCheckInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final Rq rq;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(rq.isLogout()) {
            log.info("인터셉터 -> 로그아웃 상태");
            return true;
        }

        if(rq.isLogin()) {
            CustomOAuth2User user = (CustomOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Member> member = memberService.findByUsername(user.getUsername());
            log.info("인터셉터 -> 로그인 상태({})", member.orElse(null));
            return true;
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
