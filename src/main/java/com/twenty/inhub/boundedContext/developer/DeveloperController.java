package com.twenty.inhub.boundedContext.developer;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/developer")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;
    private final Rq rq;

    //-- 개발자 페이지 홈 --//
    @GetMapping("/menu")
    public String menu() {
        log.info("open api 매뉴 페이지 응답 완료");

        return "usr/developer/top/menu";
    }

    //-- access key 발급 --//
    @GetMapping("/create")
    public String join(Model model) {
        log.info("access key 발급 요청 확인");

        Member member = rq.getMember();
        String token = developerService.create(member);

        model.addAttribute("token", token);
        log.info("access token 발급 완료 member id = {}", member.getId());
        return "usr/developer/top/create";
    }
}
