package com.twenty.inhub.boundedContext.note;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/send")
    public String sendForm(NoteSendForm form) {
        return "usr/note/send";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/send")
    public String send(NoteSendForm form) {
        log.info("문의 내용 = {}", form.getContent());
        RsData<Note> rsData = noteService.sendNote(rq.getMember(), form.getContent());

        return rq.redirectWithMsg("/member/mypage", rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String notes(Model model) {
        List<Note> notes = noteService.findByUsername(rq.getMember().getUsername());

        model.addAttribute("notes", notes);

        return "usr/note/index";
    }
}