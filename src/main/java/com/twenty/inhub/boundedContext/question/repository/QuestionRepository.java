package com.twenty.inhub.boundedContext.question.repository;

import com.twenty.inhub.boundedContext.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
