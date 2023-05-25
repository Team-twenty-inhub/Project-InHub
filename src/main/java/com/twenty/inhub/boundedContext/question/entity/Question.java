package com.twenty.inhub.boundedContext.question.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.controller.form.CreateChoiceForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateSubjectiveForm;
import com.twenty.inhub.boundedContext.underline.Underline;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Question extends BaseEntity {

    private String name;
    private String difficulty;
    private String content;
    private String oldTag;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @ManyToOne(fetch = LAZY)
    private Member member;

    @ManyToOne(fetch = LAZY)
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "question")
    private List<Underline> underlines = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "question")
    private List<Tag> tags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "question")
    private List<Choice> choiceList = new ArrayList<>();


    //-- create method --//

    // 주관식 생성 //
    public static Question createSubjective(CreateSubjectiveForm form, Member member, Category category) {
        Question question = Question.builder()
                .name(form.getName())
                .content(form.getContent())
                .oldTag(form.getTags())
                .type(QuestionType.SUBJECTIVE)
                .category(category)
                .member(member)
                .build();

        return addQuestion(member, category, question);
    }

    // 객관식 생성 //
    public static Question createChoice(CreateChoiceForm form, Member member, Category category) {
        Question question = Question.builder()
                .name(form.getName())
                .content(form.getContent())
                .oldTag(form.getTags())
                .type(QuestionType.CHOICE)
                .category(category)
                .member(member)
                .build();

        return addQuestion(member, category, question);
    }

    // question list.html 추가 //
    private static Question addQuestion(Member member, Category category, Question question) {
        member.getQuestions().add(question);
        category.getQuestions().add(question);
        return question;
    }



    //-- business logic --//

    // update name, content //
    public Question updateQuestion(String name, String content) {
        return this.toBuilder()
                .name(name)
                .content(content)
                .modifyDate(LocalDateTime.now())
                .build();
    }

    // 태그 get //
    public List<String> getTags() {
        List<String> list = new ArrayList<>();
        String[] tags = oldTag.replace(" ","").split(",");

        for (String s : tags) list.add(s);

        return list;
    }
}
