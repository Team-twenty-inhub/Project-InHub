package com.twenty.inhub.boundedContext.question.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionSearchForm {

    private Long categoryId;
    private String tag;
}
