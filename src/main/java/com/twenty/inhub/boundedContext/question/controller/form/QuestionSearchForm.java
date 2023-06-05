package com.twenty.inhub.boundedContext.question.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class QuestionSearchForm {

    private String tag;

    public QuestionSearchForm(String tag) {
        this.tag = tag;
    }
}
