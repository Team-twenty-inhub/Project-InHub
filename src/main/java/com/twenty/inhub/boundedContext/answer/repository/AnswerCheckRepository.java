package com.twenty.inhub.boundedContext.answer.repository;

import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import com.twenty.inhub.boundedContext.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerCheckRepository extends JpaRepository<AnswerCheck,Long> {

    Optional<AnswerCheck> findByQuestionId(Long questionId);
}
