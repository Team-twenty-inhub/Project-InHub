package com.twenty.inhub.boundedContext.underline;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.entity.QBook;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.QQuestion;
import com.twenty.inhub.boundedContext.question.entity.Question;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UnderlineQueryRepository {

    private final JPAQueryFactory query;
    private QUnderline underline = QUnderline.underline;
    private QQuestion question = QQuestion.question;
    private QBook book = QBook.book;

    public UnderlineQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    //-- find by book , question --//
    public List<Underline> findByBookQuestion(Book book, Question question) {

        return query
                .selectFrom(underline)
                .where(underline.question.eq(question)
                        .and(underline.book.eq(book)))
                .fetch();
    }

    //-- find by member , question --//
    public List<Underline> findByQuestionMember(Question question, Member member) {
        return query
                .selectFrom(underline)
                .where(underline.question.eq(question)
                        .and(underline.book.member.eq(member)))
                .fetch();
    }
}
