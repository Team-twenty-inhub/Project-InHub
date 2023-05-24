package com.twenty.inhub.boundedContext.question.controller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuestionController {


    @GetMapping("/test")
    public String homeTest() {
        log.info("test 요청 확인");
        return "/usr/question/testPage";
    }
}
