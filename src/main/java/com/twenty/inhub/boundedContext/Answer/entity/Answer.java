package com.twenty.inhub.boundedContext.Answer.entity;

import com.twenty.inhub.boundedContext.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;


    Integer questionAnswer;

    @Column(columnDefinition = "TEXT")
    String content;


    /* 아직 연결안된 것들
    @ManyToMany
    Set<Member> voter;


    질문 연결 예정
    @ManyToOne
     private Question question;

    카테고리 추가예정
    private Category category

     */
}
