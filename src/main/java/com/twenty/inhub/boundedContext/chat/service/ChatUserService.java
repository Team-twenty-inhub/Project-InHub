package com.twenty.inhub.boundedContext.chat.service;

import com.twenty.inhub.boundedContext.chat.entity.ChatUser;
import com.twenty.inhub.boundedContext.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final ChatUserRepository chatRoomUserRepository;

    public ChatUser findById(Long chatRoomUserId) {
        return chatRoomUserRepository.findById(chatRoomUserId).orElseThrow();
    }

    public List<ChatUser> findByChatRoom_id(Long roomId) {
        return chatRoomUserRepository.findByChatRoom_id(roomId);
    }

    public void deleteById(Long id) {
        chatRoomUserRepository.deleteById(id);
    }
}
