package com.twenty.inhub.boundedContext.book.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.underline.Underline;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Book extends BaseEntity {

    private String name;
    private String about;
    private int playCount;
    private int recommend;

    @ManyToOne(fetch = LAZY)
    private Member member;

    @OneToMany
    @Builder.Default
    private List<Underline> underlines = new ArrayList<>();


    //-- create method --//

    public static Book createBook(BookCreateForm form, Member member) {
        Book build = Book.builder()
                .name(form.getName())
                .about(form.getAbout())
                .member(member)
                .build();

        return build;
    }
}
