package com.twenty.inhub.boundedContext.book.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.entity.QBook;
import com.twenty.inhub.boundedContext.question.entity.QTag;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class BookQueryRepository {

    private final JPAQueryFactory query;
    private QBook book = QBook.book;
    private QTag tag = QTag.tag1;

    public BookQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //-- name 으로 문제집 검색 --//
    public PageResForm<Book> findByNameTag(SearchForm form) {

        BooleanBuilder builder = new BooleanBuilder();
        String input = form.getInput();
        int page = form.getPage();


        if (input != null && !input.isEmpty()) {
            BooleanExpression nameCondition = book.name.contains(input);
            BooleanExpression tagCondition = book.tagList.any().tag.contains(input);
            builder.and(nameCondition.or(tagCondition));
        }

        List<Book> books = query.selectFrom(book)
                .leftJoin(book.tagList, tag)
                .where(builder)
                .groupBy(book.id)
                .offset(page * 16)
                .limit(16)
                .fetch();

        long totalCount = query.selectFrom(book)
                .leftJoin(book.tagList, tag)
                .where(builder)
                .groupBy(book.id)
                .fetchCount();

        return new PageResForm<>(books, page, totalCount);
    }

    //-- find random books --//
    public List<Book> findRandomBooks(int random, int count) {
        NumberPath<Integer> standard;
        if (random == 0)
            standard = book.recommend;
        else {
            standard = book.playCount;
        }

        return query
                .selectFrom(book)
                .orderBy(standard.desc())
                .limit(count)
                .fetch();
    }

    //-- find by tag --//
    public PageResForm<Book> findByTag(SearchForm form) {

        BooleanBuilder builder = new BooleanBuilder();

        if(StringUtils.hasText(form.getInput()))
            builder.and(book.tagList.any().tag.like("%" + form.getInput() + "%"));

        List<Book> books = query
                .selectFrom(book)
                .leftJoin(book.tagList)
                .where(builder)
                .offset(form.getPage() * 1)
                .limit(1)
                .fetch();

        Long count = query
                .select(book.count())
                .from(book)
                .where(builder)
                .fetchOne();

        return new PageResForm(books, form.getPage(), count);
    }
}
