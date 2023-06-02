package com.twenty.inhub.boundedContext.member.service;

import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("일반 회원가입")
    void t01() {
        Member member4 = memberService.create("test1", "1234").getData();
        Member member5 = memberService.create("test2", "1234").getData();

        assertThat(member4.getId()).isEqualTo(1);
        assertThat(member4.getUsername()).isEqualTo("test1");

        assertThat(member5.getId()).isEqualTo(2);
        assertThat(member5.getUsername()).isEqualTo("test2");
    }

    @Test
    @DisplayName("답변을 10개 이상 했을 경우, SENIOR 등급 부여")
    void t02() {
        Member member1 = memberService.create("test1", "1234").getData();

        List<Answer> answers1 = member1.getAnswers();
        for (int i = 0; i < 10; i++) {
            answers1.add(Answer.builder().build());
            member1.updateRole(MemberRole.SENIOR);
        }

        assertThat(member1.getRole()).isEqualTo(MemberRole.SENIOR);
    }
}