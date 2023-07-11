package com.twenty.inhub.boundedContext.question.event;

import com.twenty.inhub.boundedContext.question.event.event.QuestionSolveEvent;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class QuestionEventListener {

    private final QuestionService questionService;

    @EventListener
    public void listen(QuestionSolveEvent event) {
        log.info("question 난이도 업데이트 이벤트 발생");
        questionService.updateDifficulty(event.getQuestionSolveList());
        log.info("question 난이도 업데이트 완료");
    }
}
