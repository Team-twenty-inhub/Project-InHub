package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
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
    private Question question;

    @ManyToOne(fetch = LAZY)
    private Book book;


    //-- create method --//
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
