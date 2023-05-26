package com.twenty.inhub.boundedContext.question.controller.form;

import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateFunctionForm {

    private List<Long> categories;
    private List<QuestionType> type;
    private Integer minDifficulty;
    private Integer maxDifficulty;
    private Integer count;

}
