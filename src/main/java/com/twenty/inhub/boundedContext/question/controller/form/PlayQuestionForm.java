package com.twenty.inhub.boundedContext.question.controller.form;

import com.twenty.inhub.boundedContext.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlayQuestionForm {

    private List<Question> playlist;
}
