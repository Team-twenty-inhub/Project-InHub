package com.twenty.inhub.base.firebase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FCMController {

    private final FirebaseInit init;

    @GetMapping("/fcm/test")
    public String test() {
        init.init();
        return "usr/firebase/firebase";
    }
}
