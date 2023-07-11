package com.twenty.inhub.boundedContext.chat.repository;

import com.twenty.inhub.boundedContext.chat.entity.ChatRoom;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByOwner_username(String username);
    void deleteById(@NotNull Long id);
}
