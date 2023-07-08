package com.twenty.inhub.boundedContext.developer;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/developer")
@RequiredArgsConstructor
public class DeveloperController {

    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/menu")
    public String menu() {
        log.info("open api 매뉴 페이지 응답 완료");

        return "usr/developer/top/menu";
    }
}
