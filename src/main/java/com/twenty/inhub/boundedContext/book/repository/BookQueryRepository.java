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

        if(StringUtils.hasText(form.getInput())){
            BooleanExpression tagCondition = book.tagList.any().tag.like("%" + form.getInput() + "%");
            BooleanExpression nameCondition = book.name.like("%" + form.getInput() + "%");
            builder.and(tagCondition.or(nameCondition));
        }


        List<Book> books = query
                .selectFrom(book)
                .leftJoin(book.tagList)
                .where(builder)
                .groupBy(book)
                .offset(form.getPage() * 16)
                .limit(16)
                .fetch();

        Long count = query
                .select(book.id.countDistinct())
                .from(book)
                .leftJoin(book.tagList)
                .where(builder)
                .fetchOne();

        return new PageResForm(books, form.getPage(), count);
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
