package com.twenty.inhub.boundedContext.community.controller;

import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.repository.CommunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.NoSuchElementException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class CommunityControllerTests {

    @Autowired
    private CommunityRepository communityRepository;
    private Community savedCommunity;

    @BeforeEach
    void setup() {
        savedCommunity = Community.builder()
                .title("제목1")
                .content("내용1")
                .name("홍길동")
                .build();
        savedCommunity = communityRepository.save(savedCommunity);
    }

    @Test
    @DisplayName("생성 기능")
    void t001() throws Exception {
        Community community = Community.builder()
                .title("신규 제목")
                .content("신규 내용")
                .name("임꺽정")
                .build();

        Community createdCommunity = communityRepository.save(community);

        assertNotNull(createdCommunity.getId());
        assertEquals(community.getTitle(), createdCommunity.getTitle());
        assertEquals(community.getContent(), createdCommunity.getContent());
    }

    @Test
    @DisplayName("가져오기 기능")
    void t002() throws Exception {
        Community retrievedCommunity = communityRepository.findById(savedCommunity.getId())
                .orElseThrow(NoSuchElementException::new);

        assertNotNull(retrievedCommunity);
        assertEquals(savedCommunity.getId(), retrievedCommunity.getId());
        assertEquals(savedCommunity.getTitle(), retrievedCommunity.getTitle());
        assertEquals(savedCommunity.getContent(), retrievedCommunity.getContent());
    }

    @Test
    @DisplayName("수정 기능")
    void t003() throws Exception {
        Community updatedCommunity = Community.builder()
                .title("수정 제목")
                .content("내용")
                .build();

        Community modifiedCommunity = communityRepository.findById(savedCommunity.getId())
                .orElseThrow(NoSuchElementException::new);
        modifiedCommunity.setTitle(updatedCommunity.getTitle());
        modifiedCommunity.setContent(updatedCommunity.getContent());
        Community savedModifiedCommunity = communityRepository.save(modifiedCommunity);

        assertNotNull(savedModifiedCommunity);
        assertEquals(savedCommunity.getId(), savedModifiedCommunity.getId());
        assertEquals(updatedCommunity.getTitle(), savedModifiedCommunity.getTitle());
        assertEquals(updatedCommunity.getContent(), savedModifiedCommunity.getContent());
    }

    @Test
    @DisplayName("삭제 기능")
    void t004() throws Exception {
        communityRepository.deleteById(savedCommunity.getId());

        assertFalse(communityRepository.existsById(savedCommunity.getId()));
    }
}