package com.twenty.inhub.boundedContext.question.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twenty.inhub.boundedContext.question.entity.QQuestion;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionQueryRepository {

    private final JPAQueryFactory query;

    public QuestionQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Question> findByChoice() {
        QQuestion question = QQuestion.question;

        return query
                .selectFrom(question)
                .where(question.type.eq(QuestionType.CHOICE))
                .fetch();
    }
}
