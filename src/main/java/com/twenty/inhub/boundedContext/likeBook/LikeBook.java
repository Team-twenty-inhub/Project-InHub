package com.twenty.inhub.boundedContext.likeBook;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.member.entity.Member;
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
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class LikeBook extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member member;

    @ManyToOne(fetch = LAZY)
    private Book book;


    //-- create method --//

    // create book //
    public static LikeBook createLikeBook(Member member, Book book) {
        LikeBook build = LikeBook.builder()
                .member(member)
                .book(book)
                .build();

        member.getLikeList().add(build);
        book.getLikeList().add(build);
        book.updateRecommend();
        return build;
    }

    //-- business method --//

    // delete //
    public void delete() {
        this.member.getLikeList().remove(this);
        this.book.getLikeList().remove(this);
        this.book.updateRecommend();
    }
}
