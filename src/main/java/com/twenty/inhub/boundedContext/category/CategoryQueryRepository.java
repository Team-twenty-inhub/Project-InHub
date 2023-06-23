package com.twenty.inhub.boundedContext.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twenty.inhub.boundedContext.member.entity.QMember;
import com.twenty.inhub.boundedContext.question.entity.QQuestion;
import com.twenty.inhub.boundedContext.underline.QUnderline;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

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

}
