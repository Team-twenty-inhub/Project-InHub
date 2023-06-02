package com.twenty.inhub.boundedContext.question.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
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

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;
import static com.twenty.inhub.boundedContext.question.entity.QuestionType.SAQ;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Question extends BaseEntity {

    private String name;
    private int difficulty;
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
    @OneToMany(mappedBy = "question", cascade = ALL)
    private List<Tag> tags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = ALL)
    private List<Choice> choiceList = new ArrayList<>();


    //-- create method --//

    // 객관식 생성 //
    public static Question createQuestion(CreateQuestionForm form, List<Choice> choices, List<Tag> tags, Member member, Category category) {
        Question question = build(form, member, category);

        for (Choice choice : choices) question.addChoice(choice);
        for (Tag tag : tags) question.addTag(tag);

        return addQuestion(member, category, question);
    }

    // 주관식 생성 //
    public static Question createQuestion(CreateQuestionForm form, List<Tag> tags, Member member, Category category) {
        Question question = build(form, member, category);

        for (Tag tag : tags) question.addTag(tag);
        return addQuestion(member, category, question);
    }

    // create //
    private static Question build(CreateQuestionForm form, Member member, Category category) {
        return Question.builder()
                .name(form.getName())
                .content(form.getContent())
                .type(form.getType())
                .category(category)
                .member(member)
                .build();
    }

    // 타입 매퍼 //
    private static QuestionType typeMapper(String type) {
        return switch (type) {
            case "true" -> MCQ;
            default -> SAQ;
        };
    }

    // tag 추가 //
    private void addTag(Tag tag) {
        this.tags.add(tag);
        tag.addQuestion(this);
    }

    // choice 추가 //
    private void addChoice(Choice choice) {
        this.choiceList.add(choice);
        choice.addQuestion(this);
    }

    // question list.html 추가 //
    public static Question addQuestion(Member member, Category category, Question question) {
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
    public List<String> getTagList() {
        List<String> list = new ArrayList<>();
        String[] tags = oldTag.replace(" ","").split(",");

        for (String s : tags) list.add(s);

        return list;
    }
}