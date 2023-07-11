package com.twenty.inhub.boundedContext.chat.repository;

import com.twenty.inhub.boundedContext.chat.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    List<ChatUser> findByChatRoom_id(Long roomId);
}
