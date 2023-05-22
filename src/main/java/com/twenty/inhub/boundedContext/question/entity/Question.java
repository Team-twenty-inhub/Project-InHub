package com.twenty.inhub.boundedContext.question.entity;

import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.Member;
import com.twenty.inhub.boundedContext.underline.Underline;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Question {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    private String name;
    private String difficulty;
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

//    @ManyToOne(fetch = LAZY)
//    private Member member;

    @ManyToOne(fetch = LAZY)
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "question")
    private List<Underline> underlines = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "question")
    private List<Tag> tags = new ArrayList<>();

//    @Builder.Default
//    @OneToMany(mappedBy = "question")
//    private List<Answer> answers = new ArrayList<>();
}
