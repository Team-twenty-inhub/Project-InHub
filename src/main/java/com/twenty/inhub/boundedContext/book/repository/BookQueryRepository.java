package com.twenty.inhub.boundedContext.book.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.entity.QBook;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookQueryRepository {

    private final JPAQueryFactory query;
    private QBook book = QBook.book;

    public BookQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //-- name 으로 문제집 검색 --//
    public List<Book> findByName(String name) {
        return query
                .selectFrom(book)
                .where(book.name.like("%" + name + "%"))
                .fetch();
    }

}
