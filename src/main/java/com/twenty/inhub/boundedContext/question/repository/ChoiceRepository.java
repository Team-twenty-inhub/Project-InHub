package com.twenty.inhub.boundedContext.question.repository;

import com.twenty.inhub.boundedContext.question.entity.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
}
