package com.twenty.inhub.boundedContext.chat.service;

import com.twenty.inhub.boundedContext.chat.dto.ChatMessageDto;
import com.twenty.inhub.boundedContext.chat.entity.ChatMessage;
import com.twenty.inhub.boundedContext.chat.entity.ChatMessageType;
import com.twenty.inhub.boundedContext.chat.entity.ChatRoom;
import com.twenty.inhub.boundedContext.chat.entity.ChatUser;
import com.twenty.inhub.boundedContext.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage createAndSave(String content, Long senderId, Long chatRoomId, ChatMessageType type) {

        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);

        ChatUser sender = chatRoom.getChatUsers().stream()
                .filter(chatUser -> chatUser.getMember().getId().equals(senderId))
                .findFirst()
                .orElseThrow();

        ChatMessage chatMessage = ChatMessage.builder()
                .content(content)
                .sender(sender)
                .type(type)
                .chatRoom(chatRoom)
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageDto> getByChatRoomIdAndUserIdAndFromId(Long roomId, Long userId, Long fromId) {

        ChatRoom chatRoom = chatRoomService.findById(roomId);

        chatRoom.getChatUsers().stream()
                .filter(chatUser -> chatUser.getMember().getId().equals(userId))
                .findFirst()
                .orElseThrow();

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(roomId);

        List<ChatMessage> list = chatMessages.stream()
                .filter(chatMessage -> chatMessage.getId() > fromId)
                .sorted(Comparator.comparing(ChatMessage::getId))
                .toList();

        return ChatMessageDto.fromChatMessages(list);
    }

    public List<ChatMessage> findAll() {
        return chatMessageRepository.findAll();
    }

    public void deleteById(Long id) {
        chatMessageRepository.deleteById(id);
    }
}
