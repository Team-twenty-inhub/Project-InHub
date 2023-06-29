package com.twenty.inhub.boundedContext.chat.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.chat.entity.ChatMessage;
import com.twenty.inhub.boundedContext.chat.entity.ChatRoom;
import com.twenty.inhub.boundedContext.chat.entity.ChatUser;
import com.twenty.inhub.boundedContext.chat.repository.ChatMessageRepository;
import com.twenty.inhub.boundedContext.chat.repository.ChatRoomRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatUserService chatUserService;

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

    public RsData<ChatRoom> deleteRoom(Member member, Long roomId) {
        // 연관관계 메세지 삭제
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(roomId);
        messages.forEach(e -> chatMessageRepository.deleteById(e.getId()));

        // 연관관계 채팅방에 접속한 유저 삭제
        List<ChatUser> chatUsers = chatUserService.findByChatRoom_id(roomId);
        chatUsers.forEach(e -> chatUserService.deleteById(e.getId()));

        ChatRoom room = findById(roomId);

        boolean permission = room.getChatUsers().stream()
                .anyMatch(chatUser -> chatUser.getMember().getId().equals(member.getId()));

        if(!permission && !member.isAdmin()) {
            return RsData.of("F-1", "해당 채팅방을 삭제할 권한이 없습니다.");
        }

        chatRoomRepository.deleteById(roomId);

        return RsData.of("S-1", "[%s] 채팅방을 삭제했습니다.".formatted(room.getName()), room);
    }
}
