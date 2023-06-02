package com.twenty.inhub.boundedContext.question.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.entity.QAnswer;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.controller.form.QuestionSearchForm;
import com.twenty.inhub.boundedContext.question.entity.QQuestion;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class QuestionQueryRepository {

    private final JPAQueryFactory query;
    private QQuestion question = QQuestion.question;
    private QAnswer answer = QAnswer.answer;

    public QuestionQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    //-- play list id 로만 조회 --//
    public List<Long> playlist(List<Long> id, List<QuestionType> type, List<Integer> difficulties, Integer count) {

        List<Long> questionIds = query.select(question.id)
                .from(question)
                .where(question.category.id.in(id)
                        .and(question.type.in(type))
                        .and(question.difficulty.in(difficulties)))
                .fetch();

        // 랜덤한 순서로 정렬하기 위해 랜덤 값을 생성하여 정렬에 활용
        long seed = System.nanoTime();
        Collections.shuffle(questionIds, new Random(seed));

        // count 만큼 잘라내기
        return questionIds.subList(0, Math.min(count, questionIds.size()));
    }

    //-- find by id --//
    public List<Question> findById(List<Long> id) {
        List<Question> questions = query
                .selectFrom(question)
                .where(question.id.in(id))
                .fetch();

        // 조화한 list 를 id 를 key 값으로 하는 map 으로 변환
        Map<Long, Question> sorter = questions.stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        // id 의 인덱스를 key 로 value 를 담는 list 를 반환
        return id.stream()
                .map(sorter::get)
                .collect(Collectors.toList());
    }

    //-- find answer by member & question 임시 매서드 --//
    public List<Answer> findAnswerByQustionMember(Question question, Member member) {

        return query
                .selectFrom(answer)
                .where(answer.question.eq(question)
                        .and(answer.member.eq(member)))
                .fetch();
    }

    //-- find by tag --//
    public List<Question> findByInput(QuestionSearchForm form) {

        BooleanBuilder builder = new BooleanBuilder();
        String tag = form.getTag();

        if (StringUtils.hasText(tag))
            builder.and(question.tags.any().tag.like("%" + tag + "%"));

        return query
                .selectFrom(question)
                .leftJoin(question.tags)
                .where(builder)
                .fetch();
    }

}


