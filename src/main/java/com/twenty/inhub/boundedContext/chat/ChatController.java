package com.twenty.inhub.boundedContext.chat;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class ChatController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chat")
    public String chatGET(){

        log.info("@ChatController, chat GET()");

        return "usr/chat/chat";
    }
}