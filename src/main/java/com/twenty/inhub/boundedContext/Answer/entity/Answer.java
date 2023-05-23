package com.twenty.inhub.boundedContext.Answer.entity;

import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    private Integer questionAnswer;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToMany
    Set<Member> voter;

    @ManyToOne
    private Question question;


    @ManyToOne(fetch = LAZY)
    private Category category;


}
