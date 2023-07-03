package com.twenty.inhub.boundedContext.note;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    @Transactional
    public RsData<Note> sendNote(Member sender, String title, String content) {
        Note note = Note.builder()
                .title(title)
                .content(content)
                .sender(sender)
                .build();

        Note saved = noteRepository.save(note);

        return RsData.of("S-1", "문의를 성공적으로 보냈습니다.", saved);
    }

    public List<Note> findByUsername(String username) {
        return noteRepository.findBySender_username(username);
    }
}