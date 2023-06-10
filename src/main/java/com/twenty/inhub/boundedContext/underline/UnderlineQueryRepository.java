package com.twenty.inhub.boundedContext.underline;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UnderlineQueryRepository {

    private final JPAQueryFactory query;
    private QUnderline underline = QUnderline.underline;

    public UnderlineQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    //-- find by member , question --//
    public List<Underline> findByMemberQuestion(Long memberId, Long questionId) {

        return query
                .selectFrom(underline)
                .where(underline.member.id.eq(memberId)
                        .and(underline.question.id.eq(questionId)))
                .fetch();
    }

    //-- find by member , question --//
    public List<Underline> findByCategory(Member member, Category category) {
        return query
                .selectFrom(underline)
                .where(underline.question.category.eq(category)
                        .and(underline.member.eq(member)))
                .fetch();
    }
}
