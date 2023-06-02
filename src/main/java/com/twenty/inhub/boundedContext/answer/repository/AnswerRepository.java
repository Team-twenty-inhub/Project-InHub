package com.twenty.inhub.boundedContext.answer.repository;

import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer,Long> {

    Optional<Answer> findByMemberIdAndQuestionId(Long MemberId,Long questionId);
}
