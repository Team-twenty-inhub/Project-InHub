package com.twenty.inhub.boundedContext.question.controller.form;

import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.underline.Underline;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateFunctionForm {

    private List<Long> categories;
    private List<QuestionType> type;
    private List<Integer> difficulties;
    private List<Underline> underlines;
    private Integer count;
}
