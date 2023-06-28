package com.twenty.inhub.boundedContext.chat.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.boundedContext.chat.entity.ChatRoom;
import com.twenty.inhub.boundedContext.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final Rq rq;

    @GetMapping("/rooms")
    public String showRooms(Model model) {

        List<ChatRoom> chatRooms = chatRoomService.findByUsername(rq.getMember().getUsername());

        if(rq.getMember().getRole().toString().equals("ADMIN")) {
            chatRooms = chatRoomService.findAll();
        }

        model.addAttribute("chatRooms", chatRooms);
        return "usr/chat/rooms";
    }

    @GetMapping("/rooms/{roomId}")
    public String showRoom(@PathVariable Long roomId, Model model) {

        ChatRoom chatRoom = chatRoomService.getByIdAndUserId(roomId, rq.getMember().getId());

        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("me", rq.getMember());

        return "usr/chat/room";
    }

    @GetMapping("/rooms/new")
    public String showNewRoom() {
        return "usr/chat/newRoom";
    }

    @PostMapping("/rooms")
    public String newRoom(String roomName) {
        ChatRoom chatRoom = chatRoomService.createAndSave(roomName, rq.getMember().getId());

        return rq.redirectWithMsg("/rooms/%d".formatted(chatRoom.getId()), "새로운 방 생성 성공");
    }
}
