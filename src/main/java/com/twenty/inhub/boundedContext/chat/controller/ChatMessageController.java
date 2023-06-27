package com.twenty.inhub.boundedContext.chat.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.security.CustomOAuth2User;
import com.twenty.inhub.boundedContext.chat.dto.ChatMessageDto;
import com.twenty.inhub.boundedContext.chat.entity.ChatMessage;
import com.twenty.inhub.boundedContext.chat.request.ChatMessageRequest;
import com.twenty.inhub.boundedContext.chat.response.SignalResponse;
import com.twenty.inhub.boundedContext.chat.service.ChatMessageService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.twenty.inhub.boundedContext.chat.entity.ChatMessageType.MESSAGE;
import static com.twenty.inhub.boundedContext.chat.response.SignalType.NEW_MESSAGE;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chats/{roomId}/sendMessage")
    @SendTo("/topic/chats/{roomId}")
    public SignalResponse sendChatMessage(@DestinationVariable Long roomId, ChatMessageRequest request,
                                          @AuthenticationPrincipal CustomOAuth2User member)  {

        log.info("content : {}", request.getContent());

        chatMessageService.createAndSave(request.getContent(), member.getId(), roomId, MESSAGE);

        return SignalResponse.builder()
                .type(NEW_MESSAGE)
                .build();
    }

    @MessageExceptionHandler
    public void handleException(Exception ex) {
        log.info("예외 발생!! = {}", ex.getMessage());
        ex.printStackTrace();
        System.out.println("예외 발생!!");
    }

    @GetMapping("/rooms/{roomId}/messages")
    @ResponseBody
    public List<ChatMessageDto> findAll(
            @PathVariable Long roomId, @RequestParam(defaultValue = "0") Long fromId,
            @AuthenticationPrincipal CustomOAuth2User member) {

        List<ChatMessageDto> chatMessageDtos =
                chatMessageService.getByChatRoomIdAndUserIdAndFromId(roomId, member.getId(), fromId);

        return chatMessageDtos;
    }
}
