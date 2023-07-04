package com.twenty.inhub.boundedContext.note.repository;

import com.twenty.inhub.boundedContext.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findBySender_Nickname(String nickname);
    List<Note> findByReceiver_Nickname(String nickname);
}
