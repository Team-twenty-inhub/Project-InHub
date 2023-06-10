package com.twenty.inhub.boundedContext.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.QMember;
import com.twenty.inhub.boundedContext.question.entity.QQuestion;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.underline.QUnderline;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryQueryRepository {

    private final JPAQueryFactory query;
    private QCategory category = QCategory.category;
    private QQuestion question = QQuestion.question;
    private QMember member = QMember.member;
    private QUnderline underline = QUnderline.underline;

    public CategoryQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //-- underline 에 포함된 카테고리만 조회하기 --//
    public List<Category> findContainUnderline(Member member, List<Question> questions) {
        return query
                .selectFrom(category)
                .join(question).on(category.eq(question.category))
                .join(underline).on(question.eq(underline.question))
                .join(this.member).on(underline.member.eq(member))
                .where(underline.question.in(questions))
                .fetch();
    }




}
