package com.twenty.inhub.boundedContext.chat.repository;

import com.twenty.inhub.boundedContext.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
