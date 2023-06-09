package com.twenty.inhub.boundedContext.mail.controller;

import com.twenty.inhub.boundedContext.mail.service.MailService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@Hidden
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    // 회원가입 이메일 인증
    @PostMapping("/mailConfirm")
    @ResponseBody
    public String mailConfirm(@RequestParam("email") String email) throws Exception {
        return mailService.sendSimpleMessageForSignUp(email);
    }

    // 기기체크 이메일 인증
    @PostMapping("/mailConfirmForRegEmail")
    @ResponseBody
    public String mailConfirmForRegEmail(@RequestParam("email") String email) throws Exception {
        return mailService.sendSimpleMessageForRegEmail(email);
    }
}
