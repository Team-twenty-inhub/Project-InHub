package com.twenty.inhub.boundedContext.note;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // 쪽지 보내는 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/send")
    public String sendForm() {
        return "usr/note/send";
    }

    // 쪽지 보내기
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/send")
    public String send(NoteSendForm form) {
        log.info("문의 내용 = {}", form.getContent());
        RsData<Note> rsData = noteService.sendNote(rq.getMember(), form.getReceiver(), form.getTitle(), form.getContent());

        return rq.redirectWithMsg("/member/mypage", rsData);
    }

    // 내가 보낸 쪽지 리스트
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/sendList")
    public String sendList(Model model) {
        List<Note> notes = noteService.findBySenderNickname(rq.getMember().getNickname());

        model.addAttribute("notes", notes);

        return "usr/note/sendList";
    }

    // 내가 받은 쪽지 리스트
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/receiveList")
    public String receiveList(Model model) {
        List<Note> notes = noteService.findByReceiverNickname(rq.getMember().getNickname());

        model.addAttribute("notes", notes);

        return "usr/note/receiveList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{noteId}")
    public String detail(@PathVariable Long noteId, Model model) {
        RsData<Note> rsData = noteService.findById(noteId);

        if(rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        model.addAttribute("note", rsData.getData());

        return "usr/note/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{noteId}")
    public String delete(@PathVariable Long noteId) {
        RsData<Note> rsData = noteService.deleteNote(rq.getMember(), noteId);

        if(rsData.isFail()) {
            return rq.historyBack("쪽지 삭제를 실패했습니다.");
        }

        if(rsData.getResultCode().equals("S-1")) {
            return rq.redirectWithMsg("/note/sendList", rsData.getMsg());
        } else {
            return rq.redirectWithMsg("/note/receiveList", rsData.getMsg());
        }
    }
}