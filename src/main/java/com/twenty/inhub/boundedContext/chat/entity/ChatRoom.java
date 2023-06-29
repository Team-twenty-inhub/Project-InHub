package com.twenty.inhub.boundedContext.chat.entity;

import com.twenty.inhub.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    private Member owner;

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true, cascade = PERSIST)
    @Builder.Default
    private Set<ChatUser> chatUsers = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true, cascade = PERSIST)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static ChatRoom create(String name, Member owner) {

        Assert.notNull(name, "name는 널일 수 없습니다.");
        Assert.notNull(owner, "owner는 널일 수 없습니다.");

        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return chatRoom;
    }

    public void addChatUser(Member owner) {
        ChatUser chatUser = ChatUser.builder()
                .member(owner)
                .chatRoom(this)
                .build();
        chatUsers.add(chatUser);
    }
}
