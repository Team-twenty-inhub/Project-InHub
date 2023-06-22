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


    //-- find by member , question --//
    public List<Underline> findByMemberQuestion(Long memberId, Long questionId) {

        return query
                .selectFrom(underline)
//                .where(underline.member.id.eq(memberId)
//                        .and(underline.question.id.eq(questionId)))
                .where(underline.question.id.eq(questionId))
                .fetch();
    }

    //-- find by member , question --//
    public List<Underline> findByCategory(Member member, Category category) {

        return query
                .selectFrom(underline)
                .where(underline.question.category.eq(category))
//                        .and(underline.member.eq(member)))
                .fetch();
    }

    //-- find by book , question --//
    public List<Underline> findByBookQuestion(Book book, Question question) {

        return query
                .selectFrom(underline)
                .where(underline.question.eq(question)
                        .and(underline.book.eq(book)))
                .fetch();
    }
}
