package com.twenty.inhub.boundedContext.book.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Tag;
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

import static jakarta.persistence.CascadeType.ALL;
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
    private String author;
    private int playCount;
    private int recommend;
    private double rate;
    private String img;

    @ManyToOne(fetch = LAZY)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = ALL)
    private List<Tag> tagList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "book")
    private List<Underline> underlines = new ArrayList<>();


    //-- create method --//

    // book 생성 //
    public static Book createBook(BookCreateForm form, Member member, List<Tag> tags) {
        Book book = Book.builder()
                .name(form.getName())
                .about(form.getAbout())
                .author(member.getNickname())
                .member(member)
                .build();

        for (Tag tag : tags) book.addTag(tag);
        member.getBooks().add(book);
        return book;
    }

    // 커버 image 생성 //
    public void createImg(String img) {
        this.img = img;
    }

    // tag 추가 //
    private void addTag(Tag tag) {
        this.tagList.add(tag);
        tag.addBook(this);
    }
}
