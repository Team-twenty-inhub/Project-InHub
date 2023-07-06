package com.twenty.inhub.boundedContext.question.event.dto;

import com.twenty.inhub.boundedContext.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSolveDto {

    private Question question;
    private double score;
}
