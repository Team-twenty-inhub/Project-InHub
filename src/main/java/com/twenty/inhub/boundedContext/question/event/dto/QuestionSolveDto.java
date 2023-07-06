package com.twenty.inhub.boundedContext.question.event.dto;

import com.twenty.inhub.boundedContext.question.entity.Question;
import lombok.Getter;


@Getter
public class QuestionSolveDto {

    private Question question;
    private double score;
}
