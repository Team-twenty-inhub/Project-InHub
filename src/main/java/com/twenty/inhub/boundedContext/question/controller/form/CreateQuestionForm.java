package com.twenty.inhub.boundedContext.question.controller.form;

import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateQuestionForm {

    private String name;
    private String content;
    private String tag;
    private List<String> choiceList;
    private Long categoryId;
    private QuestionType type;


    public List<String> getTags() {
        return List.of(
                this.tag.replace(" ", "")
                        .split(",")
        );
    }
}
