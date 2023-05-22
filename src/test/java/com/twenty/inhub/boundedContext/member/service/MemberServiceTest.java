package com.twenty.inhub.boundedContext.member.service;

import com.twenty.inhub.boundedContext.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void 회원가입() {
        Member member1 = memberService.create("    ", "1234").getData();
        Member member2 = memberService.create("test2", "    ").getData();
        Member member3 = memberService.create("    ", "    ").getData();
        Member member4 = memberService.create("test4", "1234").getData();
        Member member5 = memberService.create("test5", "1234").getData();

        assertThat(member4.getId()).isEqualTo(1);
        assertThat(member4.getUsername()).isEqualTo("test4");

        assertThat(member5.getId()).isEqualTo(2);
        assertThat(member5.getUsername()).isEqualTo("test5");
    }
}