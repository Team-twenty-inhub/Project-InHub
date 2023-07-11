package com.twenty.inhub.boundedContext.question.controller.dto;

import com.twenty.inhub.boundedContext.question.entity.Question;
import lombok.Data;

@Data

public class QuestionResDto {

    private Long id;
    private String name;
    private String content;

    public QuestionResDto(Question question) {
        this.id = question.getId();
        this.name = question.getName();
        this.content = question.getContent();
    }
}
