package com.twenty.inhub.boundedContext.note.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.note.entity.Note;
import com.twenty.inhub.boundedContext.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final MemberService memberService;

    @Transactional
    public RsData<Note> sendNote(Member sender, String receiverNickname, String title, String content) {
        Optional<Member> opReceiver = memberService.findByNickname(receiverNickname);

        if(opReceiver.isEmpty()) {
            return RsData.of("F-1", "%s는 존재하지 않는 닉네임입니다.".formatted(receiverNickname));
        }

        Note note = Note.builder()
                .title(title)
                .content(content)
                .sender(sender)
                .receiver(opReceiver.get())
                .build();

        Note saved = noteRepository.save(note);

        return RsData.of("S-1", "쪽지를 성공적으로 보냈습니다.", saved);
    }

    @Transactional
    public RsData<Note> deleteNote(Member member, Long noteId) {
        Optional<Note> opNote = noteRepository.findById(noteId);
        if(opNote.isEmpty()) {
            return RsData.of("F-1", "존재하지 않는 쪽지입니다.");
        }

        Note note = opNote.get();
        RsData<Note> rsData = null;

        if(member.getNickname().equals(note.getSender().getNickname())) {
            note.setDeleteSender(true);
            rsData = RsData.of("S-1", "보낸 쪽지가 삭제되었습니다.");
        } else {
            note.setDeleteReceiver(true);
            rsData = RsData.of("S-2", "받은 쪽지가 삭제되었습니다.");
        }

        if(note.isDeleteSender() && note.isDeleteReceiver()) {
            noteRepository.delete(note);
        }

        return rsData;
    }

    @Transactional
    public List<RsData<Note>> deleteAll(Member member, Long[] deleteId) {
        return Arrays.stream(deleteId)
                .map(id -> deleteNote(member, id))
                .collect(Collectors.toList());
    }

    public Page<Note> findBySenderNickname(String nickname, int page) {
        Pageable pageable = PageRequest.of(page, 5);

        return noteRepository.findBySender_NicknameAndIsDeleteSenderOrderByCreateDateDesc(nickname, false, pageable);
    }

    public Page<Note> findByReceiverNickname(String nickname, int page) {
        Pageable pageable = PageRequest.of(page, 5);

        return noteRepository.findByReceiver_NicknameAndIsDeleteReceiverOrderByCreateDateDesc(nickname, false, pageable);
    }

    public RsData<Note> findById(Long noteId) {
        Optional<Note> opNote = noteRepository.findById(noteId);

        return opNote.map(RsData::of).orElseGet(() -> RsData.of("F-1", "해당 쪽지가 존재하지 않습니다."));
    }

    @Transactional
    public void read(Note note) {
        note.setReadDate(LocalDateTime.now());
    }
}