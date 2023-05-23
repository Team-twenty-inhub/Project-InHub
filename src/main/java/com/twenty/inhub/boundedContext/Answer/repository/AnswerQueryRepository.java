package com.twenty.inhub.boundedContext.Answer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class AnswerQueryRepository {

    private final JPAQueryFactory query;
    public AnswerQueryRepository(EntityManager em){
        this.query = new JPAQueryFactory(em);
    }
}
