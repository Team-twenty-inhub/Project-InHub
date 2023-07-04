package com.twenty.inhub.boundedContext.note.repository;

import com.twenty.inhub.boundedContext.note.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findBySender_NicknameAndIsDeleteSenderOrderByCreateDateDesc(String nickname, boolean isDeleted, Pageable pageable);
    Page<Note> findByReceiver_NicknameAndIsDeleteReceiverOrderByCreateDateDesc(String nickname, boolean isDeleted, Pageable pageable);
}
