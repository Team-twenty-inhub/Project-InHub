package com.twenty.inhub.boundedContext.chat.repository;

import com.twenty.inhub.boundedContext.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomId(Long roomId);
}
