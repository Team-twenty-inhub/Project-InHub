package com.twenty.inhub.boundedContext.question.service;

import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.repository.QuestionQueryRepository;
import com.twenty.inhub.boundedContext.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    // 간단한 crud , find by ..
    private final QuestionRepository questionRepository;

    // 복잡한 쿼리
    private final QuestionQueryRepository questionQueryRepository;

    public List<Question> findByChoice() {
        return questionQueryRepository.findByChoice();
    }
}
