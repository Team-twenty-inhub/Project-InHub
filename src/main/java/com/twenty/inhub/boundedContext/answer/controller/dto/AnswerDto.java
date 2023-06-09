package com.twenty.inhub.boundedContext.answer.controller.dto;

import lombok.Data;

@Data
public class AnswerDto {

    private String name;
    private String categoryName;
    private Enum Type;

    private String content;
    String result;
}
