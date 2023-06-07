package com.twenty.inhub.boundedContext.home;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String showMain() {
        log.info("홈페이지 접속 요청 확인");
        return "/usr/index";
    }
}
