package com.twenty.inhub.boundedContext.Answer.repository;

import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
}
