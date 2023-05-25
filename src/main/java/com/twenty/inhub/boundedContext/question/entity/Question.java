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
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Question extends BaseEntity {

    private String name;
    private String difficulty;
    private String content;
    private String choice;
    private String tag;

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


    //-- create method --//

    // 주관식 생성 //
    public static Question createSubjective(CreateSubjectiveForm form, Member member, Category category) {
        Question question = Question.builder()
                .name(form.getName())
                .content(form.getContent())
                .tag(form.getTags().replace(" ", ""))
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
                .choice(form.getChoice())
                .tag(form.getTags().replace(" ", ""))
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

    // update choice //
    public Question updateQuestion(String choice) {
        return this.toBuilder()
                .choice(choice)
                .modifyDate(LocalDateTime.now())
                .build();
    }

    // 태그 get //
    public List<String> getTags() {
        List<String> list = new ArrayList<>();
        String[] tags = tag.split(",");

        for (String s : tags) list.add(s);

        return list;
    }

    // 객관식 선택지 get //
    public List<String> getChoiceList() {
        List<String> list = new ArrayList<>();
        String[] tags = choice.split("#");

        for (String s : tags) list.add(s);

        return list;
    }
}
