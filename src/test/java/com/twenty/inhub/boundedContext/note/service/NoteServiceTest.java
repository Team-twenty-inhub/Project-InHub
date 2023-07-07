package com.twenty.inhub.boundedContext.note.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.controller.form.MemberJoinForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.note.entity.Note;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class NoteServiceTest {

    @Autowired
    private NoteService noteService;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("쪽지 보내기")
    void t01() {
        Note note = noteService.sendNote(member().getNickname(), admin().getNickname(), "테스트 쪽지", "쪽지 내용입니다.").getData();

        assertThat(note.getSender().getNickname()).isEqualTo("test1_nickname");
    }

    @Test
    @DisplayName("쪽지 삭제(단건)")
    void t02() {
        Member member = member();
        Member admin = admin();

        Note note1 = noteService.sendNote(member.getNickname(), admin.getNickname(), "테스트 쪽지1", "쪽지 내용입니다.").getData();
        Note note2 = noteService.sendNote(member.getNickname(), admin.getNickname(), "테스트 쪽지2", "쪽지 내용입니다.").getData();
        Note note3 = noteService.sendNote(member.getNickname(), admin.getNickname(), "테스트 쪽지3", "쪽지 내용입니다.").getData();

        // 쪽지 데이터(DB) -> 삭제 전 : 3개
        assertThat(noteService.findAll().size()).isEqualTo(3);

        // 발신자 측 쪽지 삭제
        noteService.deleteNote(member, note2.getId());

        // 쪽지 데이터(DB 입장) -> 삭제 후 : 3개 -> 수신자는 삭제하지 않았기 때문에 변동 없음
        assertThat(noteService.findAll().size()).isEqualTo(3);
        // 쪽지 데이터(발신자 입장) -> 삭제 후 : 2개 -> 발신자 측에서는 삭제했기 때문에 2개가 됨
        assertThat(noteService.findBySenderNickname(member.getNickname(), 0).getTotalElements()).isEqualTo(2);

        // 수신자 측 쪽지 삭제
        noteService.deleteNote(admin, note2.getId());

        // 쪽지 데이터(DB 입장) -> 삭제 후 : 2개 -> 양쪽 모두 삭제를 했으므로 완전 삭제
        assertThat(noteService.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("쪽지 삭제(다건)")
    void t03() {
        Member member = member();
        Member admin = admin();

        Note note1 = noteService.sendNote(member.getNickname(), admin.getNickname(), "테스트 쪽지1", "쪽지 내용입니다.").getData();
        Note note2 = noteService.sendNote(member.getNickname(), admin.getNickname(), "테스트 쪽지2", "쪽지 내용입니다.").getData();
        Note note3 = noteService.sendNote(member.getNickname(), admin.getNickname(), "테스트 쪽지3", "쪽지 내용입니다.").getData();

        // 쪽지 데이터(DB) -> 삭제 전 : 3개
        assertThat(noteService.findAll().size()).isEqualTo(3);

        // 발신자 측 쪽지 삭제
        noteService.deleteAll(member, new Long[]{note2.getId(), note3.getId()});

        // 쪽지 데이터(DB 입장) -> 삭제 후 : 3개 -> 수신자는 삭제하지 않았기 때문에 변동 없음
        assertThat(noteService.findAll().size()).isEqualTo(3);
        // 쪽지 데이터(발신자 입장) -> 삭제 후 : 1개 -> 발신자 측에서는 삭제했기 때문에 1개가 됨
        assertThat(noteService.findBySenderNickname(member.getNickname(), 0).getTotalElements()).isEqualTo(1);

        // 수신자 측 쪽지 삭제
        noteService.deleteAll(admin, new Long[]{note2.getId(), note3.getId()});

        // 쪽지 데이터(DB 입장) -> 삭제 후 : 1개 -> 양쪽 모두 삭제를 했으므로 완전 삭제
        assertThat(noteService.findAll().size()).isEqualTo(1);
    }

    private Member member() {
        return memberService.create(new MemberJoinForm("test1", "1234", "test1@test.com", "test1_nickname"), "").getData();
    }

    private Member admin() {
        return memberService.create(new MemberJoinForm("admin", "1234", "admin@test.com", "admin"), "").getData();
    }
}