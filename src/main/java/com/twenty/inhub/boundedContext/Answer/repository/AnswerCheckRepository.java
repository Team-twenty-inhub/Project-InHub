package com.twenty.inhub.boundedContext.Answer.repository;

import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import com.twenty.inhub.boundedContext.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerCheckRepository extends JpaRepository<Answer,Long> {

    Optional<Answer> findByQuestion(Question question);
}
