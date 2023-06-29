package com.twenty.inhub.boundedContext.chat.service;

import com.twenty.inhub.boundedContext.chat.entity.ChatRoom;
import com.twenty.inhub.boundedContext.chat.repository.ChatRoomRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;

    public ChatRoom createAndSave(String name, Long ownerId) {

        Member owner = memberService.findById(ownerId).orElseThrow();

        ChatRoom chatRoom = ChatRoom.create(name, owner);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 채팅방 사용자에 방 만든 유저 추가
        chatRoom.addChatUser(owner);
        // 채팅방 사용자에 관리자 추가
        Optional<Member> admin = memberService.findByUsername("admin");
        chatRoom.addChatUser(admin.get());

        return savedChatRoom;
    }

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public List<ChatRoom> findByUsername(String username) {
        return chatRoomRepository.findByOwner_username(username);
    }

    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow();
    }

    public ChatRoom getByIdAndUserId(Long roomId, Long userId) {
        ChatRoom chatRoom = findById(roomId);

        // 임시
        if(userId == 1) {
            return chatRoom;
        }

        chatRoom.getChatUsers().stream()
                .filter(chatUser -> chatUser.getMember().getId().equals(userId))
                .findFirst()
                .orElseThrow();

        return chatRoom;
    }
}
