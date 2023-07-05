package com.twenty.inhub.base.firebase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FCMController {

    private final FirebaseInit init;

    @GetMapping("/fcm/test")
    public String test() {
        init.init();
        return "usr/firebase/firebase";
    }

    @ResponseBody
    @PostMapping("/fcm/token")
    public String addToken(
            @RequestBody String token
    ) {
        log.info("token 저장 요청 확인 token = {}", token);

        return "good";
    }
}
