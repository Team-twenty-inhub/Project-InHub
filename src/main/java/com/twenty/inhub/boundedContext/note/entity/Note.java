package com.twenty.inhub.boundedContext.note.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Note extends BaseEntity {

    private String title;
    private String content;

    @ManyToOne(fetch = LAZY)
    private Member sender;
    @ManyToOne(fetch = LAZY)
    private Member receiver;

    @Setter
    private boolean isDeleteSender;
    @Setter
    private boolean isDeleteReceiver;

    @Setter
    private LocalDateTime readDate;
}
