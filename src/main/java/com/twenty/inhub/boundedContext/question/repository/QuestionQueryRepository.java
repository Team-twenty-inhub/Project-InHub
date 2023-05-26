package com.twenty.inhub.boundedContext.question.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twenty.inhub.boundedContext.question.entity.QQuestion;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Repository
public class QuestionQueryRepository {

    private final JPAQueryFactory query;

    public QuestionQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Question> play(List<Long> id, List<QuestionType> type, List<Integer> difficulties, Integer count) {
        QQuestion question = QQuestion.question;

        List<Question> questions = query.selectFrom(question)
                .where(question.category.id.in(id)
                        .and(question.type.in(type))
                        .and(question.difficulty.in(difficulties)))
                .fetch();

        // 랜덤한 순서로 정렬하기 위해 랜덤 값을 생성하여 정렬에 활용
        long seed = System.nanoTime();
        Collections.shuffle(questions, new Random(seed));

        // count 만큼 잘라내기
        return questions.subList(0, Math.min(count, questions.size()));
    }
}



