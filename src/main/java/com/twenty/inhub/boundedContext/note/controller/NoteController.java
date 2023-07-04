package com.twenty.inhub.boundedContext.note.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.note.controller.form.NoteSendForm;
import com.twenty.inhub.boundedContext.note.service.NoteService;
import com.twenty.inhub.boundedContext.note.entity.Note;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public String send(@Valid NoteSendForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return rq.historyBack("제목, 내용 또는 닉네임이 비어 있습니다.");
        }

        log.info("보낸 쪽지 제목 = {}", form.getTitle());
        log.info("발신자 닉네임 = {}", rq.getMember().getNickname());
        log.info("수신자 닉네임 = {}", form.getReceiver());
        RsData<Note> rsData = noteService.sendNote(rq.getMember(), form.getReceiver(), form.getTitle(), form.getContent());

        if(rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

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

    // 쪽지 상세 보기
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

    // 쪽지 단건 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{noteId}")
    public String delete(@PathVariable Long noteId) {
        log.info("삭제할 쪽지 ID = {}", noteId);
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

    // 쪽지 다중 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/deleteAll")
    public String deleteAll(Long[] deleteId) {
        log.info("삭제할 쪽지 ID 목록 = {}", Arrays.toString(deleteId));
        List<RsData<Note>> rsDataList = noteService.deleteAll(rq.getMember(), deleteId);

        List<String> resultCodes = rsDataList.stream()
                .map(RsData::getResultCode)
                .collect(Collectors.toList());

        if(resultCodes.contains("S-1")) {
            return rq.redirectWithMsg("/note/sendList", "모든 보낸 쪽지가 삭제되었습니다.");
        } else if(resultCodes.contains("S-2")) {
            return rq.redirectWithMsg("/note/receiveList", "모든 받은 쪽지가 삭제되었습니다.");
        } else {
            return rq.historyBack(rsDataList.get(0).getMsg());
        }
    }
}