package com.twenty.inhub.boundedContext.chat.repository;

import com.twenty.inhub.boundedContext.chat.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
}
