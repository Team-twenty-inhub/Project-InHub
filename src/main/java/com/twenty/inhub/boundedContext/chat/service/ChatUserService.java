package com.twenty.inhub.boundedContext.chat.service;

import com.twenty.inhub.boundedContext.chat.entity.ChatUser;
import com.twenty.inhub.boundedContext.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final ChatUserRepository chatRoomUserRepository;

    public ChatUser findById(Long chatRoomUserId) {
        return chatRoomUserRepository.findById(chatRoomUserId).orElseThrow();
    }
}
