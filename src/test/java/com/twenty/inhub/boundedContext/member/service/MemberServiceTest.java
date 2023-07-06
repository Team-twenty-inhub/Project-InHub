package com.twenty.inhub.boundedContext.member.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.member.controller.form.MemberJoinForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private  MemberService memberService;

    @Test
    @DisplayName("일반 회원가입")
    void t01() {
        Member member1 = memberService.create(new MemberJoinForm("test1", "1234", "", "test1")).getData();
        Member member2 = memberService.create(new MemberJoinForm("test2", "1234", "", "test2")).getData();
        int count = memberService.findAll().size();

        assertThat(member1.getUsername()).isEqualTo("test1");
        assertThat(member2.getUsername()).isEqualTo("test2");
        assertThat(count).isEqualTo(2);
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

    @Test
    @DisplayName("포인트 기준 랭킹 조회")
    void t03() {
        Member member1 = memberService.create(new MemberJoinForm("test1", "1234", "", "test1")).getData();
        Member member2 = memberService.create(new MemberJoinForm("test2", "1234", "", "test2")).getData();
        Member member3 = memberService.create(new MemberJoinForm("test3", "1234", "", "test3")).getData();

        memberService.increasePoint(member1, 50);
        memberService.increasePoint(member2, 10);
        memberService.increasePoint(member3, 100);

        int member1Ranking = memberService.getRanking(member1);
        int member2Ranking = memberService.getRanking(member2);
        int member3Ranking = memberService.getRanking(member3);

        assertThat(member1Ranking).isEqualTo(2);
        assertThat(member2Ranking).isEqualTo(3);
        assertThat(member3Ranking).isEqualTo(1);
    }

    @Test
    @DisplayName("가입 시 사용한 이메일로 아이디 찾기")
    void t04() {
        Member member1 = memberService.create(new MemberJoinForm("test1", "1234", "test@test.com", "test1")).getData();
        Member member2 = memberService.create(new MemberJoinForm("test2", "1234", "fake@fake.com", "test2")).getData();
        Member member3 = memberService.create(new MemberJoinForm("test3", "1234", "test@test.com", "test3")).getData();

        RsData<List<String>> myIds = memberService.findMyIds("test@test.com");

        assertThat(myIds.getData().size()).isEqualTo(2);
    }
}