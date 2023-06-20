package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.underline.dto.UnderlineCreateForm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Underline extends BaseEntity {

    private String about;

    @ManyToOne(fetch = LAZY)
    private Member member;

    @ManyToOne(fetch = LAZY)
    private Book book;

    @ManyToOne(fetch = LAZY)
    private Question question;

    //-- create method --//

    // member 에 밑줄 저장 (삭제예정) //
    protected static Underline createUnderline(String about, Member member, Question question) {
        Underline build = Underline.builder()
                .question(question)
                .member(member)
                .about(about)
                .build();

        member.getUnderlines().add(build);
        question.getUnderlines().add(build);
        return build;
    }

    // Book 에 밑줄 저장 //
    protected static Underline createUnderline(UnderlineCreateForm form, Book book, Question question) {
        Underline build = Underline.builder()
                .about(form.getAbout())
                .question(question)
                .book(book)
                .build();

        book.getUnderlines().add(build);
        question.getUnderlines().add(build);
        return build;
    }

    //-- business method --//

    // delete //
    protected void delete() {
        this.member.getUnderlines().remove(this);
        this.question.getUnderlines().remove(this);
        this.member = null;
        this.question = null;
    }

    // update about //
    public void updateAbout(String about) {
        this.about = about;
    }

}
