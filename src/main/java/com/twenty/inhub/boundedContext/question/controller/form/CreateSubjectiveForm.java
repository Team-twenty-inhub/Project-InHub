package com.twenty.inhub.boundedContext.question.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateSubjectiveForm {

    private String name;
    private String content;
    private String tags;
}
