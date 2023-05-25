package com.twenty.inhub.boundedContext.question.controller.form;

import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateQuestionForm {

    private String name;
    private String content;
    private QuestionType type;
    private List<String> tags;
    private List<String> choiceList;
}
