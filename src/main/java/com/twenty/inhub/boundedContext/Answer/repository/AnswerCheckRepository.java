package com.twenty.inhub.boundedContext.Answer.repository;

import com.twenty.inhub.boundedContext.Answer.entity.AnswerCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerCheckRepository extends JpaRepository<AnswerCheck,Long> {

    Optional<AnswerCheck> findByQuestionId(Long questionId);
}
