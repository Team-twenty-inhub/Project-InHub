package com.twenty.inhub.boundedContext.community.service;

import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.repository.CommunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CommunityServiceTests {

    @Autowired
    CommunityRepository communityRepository;
    private Community savedCommunity;

    @BeforeEach
    void setUp() {
        Community community = Community.builder()
                .title("제목1")
                .content("내용1")
                .name("홍길동")
                .build();
        savedCommunity = communityRepository.save(community);
    }

    @Test
    @DisplayName("정보 조회")
    void t001() throws Exception {
        Community retrievedCommunity = communityRepository.findById(savedCommunity.getId())
                .orElseThrow(() -> new RuntimeException("Community not found"));

        assertThat(retrievedCommunity.getId()).isEqualTo(savedCommunity.getId());
        assertThat(retrievedCommunity.getTitle()).isEqualTo("제목1");
        assertThat(retrievedCommunity.getContent()).isEqualTo("내용1");
        assertThat(retrievedCommunity.getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("게시글 수정")
    void t002() throws Exception {
        savedCommunity.setTitle("제목2");
        savedCommunity.setContent("내용2");
        Community updatedCommunity = communityRepository.save(savedCommunity);

        assertThat(updatedCommunity.getId()).isEqualTo(savedCommunity.getId());
        assertThat(updatedCommunity.getTitle()).isEqualTo("제목2");
        assertThat(updatedCommunity.getContent()).isEqualTo("내용2");
        assertThat(updatedCommunity.getName()).isEqualTo(savedCommunity.getName());
    }

    @Test
    @DisplayName("게시글 삭제")
    void t003() throws Exception {
        communityRepository.delete(savedCommunity);

        assertThat(communityRepository.findById(savedCommunity.getId())).isEmpty();
    }
}
