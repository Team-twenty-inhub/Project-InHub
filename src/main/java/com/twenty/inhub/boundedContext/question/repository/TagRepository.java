package com.twenty.inhub.boundedContext.question.repository;

import com.twenty.inhub.boundedContext.question.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
