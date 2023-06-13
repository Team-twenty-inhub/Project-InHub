package com.twenty.inhub.boundedContext.question.controller.form;

import com.twenty.inhub.boundedContext.underline.Underline;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class QuestionSearchForm {

    private String tag;
    private int select;
    private List<Underline> underlines;

    public QuestionSearchForm(String tag) {
        this.tag = tag;
    }
}
