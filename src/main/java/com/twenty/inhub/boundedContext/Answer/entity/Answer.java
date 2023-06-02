package com.twenty.inhub.boundedContext.Answer.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Answer extends BaseEntity {

    /**
     * Who Write Answer
     */
    @ManyToOne
    private Member member;

    /**
     * Keyword Matching
     * word
     */
    String word1;
    String word2;
    String word3;

    /**
     * descriptive Answer
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToMany
    Set<Member> voter;

    @ManyToOne
    private Question question;

    String result;


    public void modifyContent(String content){
        this.content = content;
    }



    public void modifyresult(String result){
        this.result = result;
    }
}