package com.twenty.inhub.boundedContext.chat.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.chat.entity.ChatRoom;
import com.twenty.inhub.boundedContext.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String showRooms(Model model) {

        List<ChatRoom> chatRooms = chatRoomService.findByUsername(rq.getMember().getUsername());

        if(rq.getMember().getRole().toString().equals("ADMIN")) {
            chatRooms = chatRoomService.findAll();
        }

        model.addAttribute("chatRooms", chatRooms);
        return "usr/chat/rooms";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{roomId}")
    public String showRoom(@PathVariable Long roomId, Model model) {

        ChatRoom chatRoom = chatRoomService.getByIdAndUserId(roomId, rq.getMember().getId());

        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("me", rq.getMember());

        return "usr/chat/room";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String showNewRoom() {
        return "usr/chat/newRoom";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String newRoom(String roomName) {
        ChatRoom chatRoom = chatRoomService.createAndSave(roomName, rq.getMember().getId());

        return rq.redirectWithMsg("/rooms/%d".formatted(chatRoom.getId()), "새로운 방 생성 성공");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{roomId}")
    public String deleteRoom(@PathVariable Long roomId) {
        RsData<ChatRoom> rsData = chatRoomService.deleteRoom(rq.getMember(), roomId);
        log.info("{}번 채팅방 삭제 결과 = {}", roomId, rsData.getResultCode());

        if(rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        log.info("삭제한 채팅방({}) = {}", rq.getMember().getUsername(), rsData.getData().getName());

        return rq.redirectWithMsg("/rooms", rsData.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/disabled/{roomId}")
    public String disabledRoom(@PathVariable Long roomId, Model model) {
        RsData<ChatRoom> rsData = chatRoomService.finished(roomId);

        model.addAttribute("chatRoom", rsData.getData());

        log.info("해당 채팅방 disabled 상태 = {}", rsData.getData().isDisabled());

        return rq.redirectWithMsg("/rooms/"+roomId, "채팅방이 비활성화 되었습니다.");
    }
}
