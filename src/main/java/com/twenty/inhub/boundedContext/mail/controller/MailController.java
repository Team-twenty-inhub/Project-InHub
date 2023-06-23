package com.twenty.inhub.boundedContext.mail.controller;

import com.twenty.inhub.boundedContext.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    // 이메일 인증
    @PostMapping("/member/login/mailConfirm")
    @ResponseBody
    public String mailConfirm(@RequestParam("email") String email) throws Exception {
        return mailService.sendSimpleMessage(email);
    }
}
