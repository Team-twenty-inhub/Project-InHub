package com.twenty.inhub.boundedContext.answer.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
public class AnswerCheck extends BaseEntity {
    /**
     * Who Write Answer
     */
    @ManyToOne
    private Member member;

    /**
     * not use
     *
     */
    String word1;
    String word2;
    String word3;


    /**
     * keyword matching
     * wordList
     */
    @Builder.Default
    @OneToMany(mappedBy = "answercheck",cascade = REMOVE)
    private List<Keyword> keywords = new ArrayList<>();



    /**
     * descriptive Answer
     */
    @Column(columnDefinition = "TEXT")
    @Setter
    private String content;

    @ManyToMany
    Set<Member> voter;

    @OneToOne(fetch = LAZY)
    private Question question;

    public void modifyContent(String content){
        this.content = content;
    }

    public int MCQContent(){
        return Integer.parseInt(this.content);
    }
}
