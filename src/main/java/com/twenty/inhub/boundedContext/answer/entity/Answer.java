package com.twenty.inhub.boundedContext.answer.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.util.Set;

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

    //점수 체크용
    private int score;


    //정답 오답 적기용
    private String result;


    private String feedback;




    public void modifyContent(String content){
        this.content = content;
    }

    public void addFeedback(String feedback){this.feedback = feedback;}

    public void modifyFeedback(String feedback){this.feedback =feedback;}

    public void modifyResult(String result){
        this.result = result;
    }

    public void updateScore(int score){
        this.score = score;
    }


    public Question getQuestion() {
        return question;
    }
}