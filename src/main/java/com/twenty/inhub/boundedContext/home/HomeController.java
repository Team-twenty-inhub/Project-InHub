package com.twenty.inhub.boundedContext.home;

import com.twenty.inhub.boundedContext.book.service.BookService;
import lombok.extern.slf4j.Slf4j;
import com.twenty.inhub.base.request.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final Rq rq;

    @GetMapping("/")
    public String showMain(Model model) {
        log.info("홈페이지 접속 요청 확인");

        if (rq.isLogin()) {

            model.addAttribute("books", rq.getMember().getBooks());
        }

        log.info("홈페이지 응답 완료");
        return "usr/index";
    }

    @GetMapping("/changeDark")
    public String changeDark() {
        rq.setThemeByCookie("dark");

        return rq.redirectBackWithMsg("다크모드로 변경되었습니다.");
    }

    @GetMapping("/changeLight")
    public String changeLight() {
        rq.setThemeByCookie("light");

        return rq.redirectBackWithMsg("라이트모드로 변경되었습니다.");
    }
}
