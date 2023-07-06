package com.twenty.inhub.boundedContext.book.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.question.entity.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class BookTag extends BaseEntity {

    private String tag;

    @ManyToOne(fetch = LAZY)
    private Book book;

    //-- create --//
    public static BookTag createTag(String tag) {
        return BookTag.builder()
                .tag(tag)
                .build();
    }

    //-- add book --//
    public void addBook(Book book) {
        this.book = book;
    }

    //-- update --//
    public BookTag updateTag(String tag) {
        this.tag = tag;
        return this;
    }
}
