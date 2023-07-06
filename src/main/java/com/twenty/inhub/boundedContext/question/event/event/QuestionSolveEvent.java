package com.twenty.inhub.boundedContext.question.event.event;

import com.twenty.inhub.boundedContext.question.event.dto.QuestionSolveDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class QuestionSolveEvent extends ApplicationEvent {

    private List<QuestionSolveDto> questionSolveList;

    public QuestionSolveEvent(Object source, List<QuestionSolveDto> questionSolveList) {
        super(source);
        this.questionSolveList = questionSolveList;
    }
}
