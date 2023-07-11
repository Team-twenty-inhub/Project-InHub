package com.twenty.inhub.boundedContext.book.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.controller.form.BookUpdateForm;
import com.twenty.inhub.boundedContext.book.event.event.BookSolveEvent;
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
import static java.time.LocalDateTime.now;
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
    private int challenger;
    private int recommend;
    private double totalScore;
    private double accuracy; // 정답률
    private String img;

    @ManyToOne(fetch = LAZY)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = ALL, orphanRemoval = true)
    private List<BookTag> tagList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "book")
    private List<Underline> underlines = new ArrayList<>();


    //-- CREATE METHOD --//

    // book 생성 //
    public static Book createBook(BookCreateForm form, Member member, List<BookTag> tags) {
        Book book = Book.builder()
                .name(form.getName())
                .about(form.getAbout())
                .author(member.getNickname())
                .member(member)
                .build();

        for (BookTag tag : tags) book.addTag(tag);
        member.getBooks().add(book);
        return book;
    }

    // 기본 book 생성 //
    public static Book createBook(Member member) {
        Book book = Book.builder()
                .name("기본 문제집")
                .about("밑줄 문제 모음집")
                .author(member.getNickname())
                .member(member)
                .build();

        member.getBooks().add(book);
        return book;
    }

    // 커버 image 생성 //
    public void createImg(String img) {
        this.img = img;
    }

    // tag 추가 //
    private void addTag(BookTag tag) {
        this.tagList.add(tag);
        tag.addBook(this);
    }


    //-- BUSINESS METHOD --//

    // update img, name, about, tag //
    public Book update(BookUpdateForm form, List<BookTag> tags) {

        Book book = this.toBuilder()
                .img(form.getImg())
                .name(form.getName())
                .about(form.getAbout())
                .modifyDate(now())
                .build();

        this.tagList.clear();
        for (BookTag tag : tags) book.addTag(tag);

        return book;
    }

    // update accuracy & challenger & total score //
    public void updateAccuracy(Book book, BookSolveEvent event) {

        book.totalScore = book.totalScore + event.getScore();
        book.challenger++;
        book.accuracy = book.totalScore / book.challenger;
    }
}
