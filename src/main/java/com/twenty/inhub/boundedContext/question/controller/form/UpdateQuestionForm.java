package com.twenty.inhub.boundedContext.question.controller.form;

import com.twenty.inhub.boundedContext.question.entity.Choice;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.Tag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateQuestionForm {

    private String name;
    private String content;
    private List<String> choiceList =  new ArrayList<>();

    private String tag;

    public void setForm(Question question) {
        this.name = question.getName();
        this.content = question.getContent();

        List<Choice> choiceList = question.getChoiceList();
        List<Tag> tags = question.getTags();

        for (Choice choice : choiceList)
            this.choiceList.add(choice.getChoice());

        if (tag.isEmpty()) tag = "";

        for (Tag tag : tags)
            this.tag = this.tag + ", " + tag.getTag();

        this.tag = this.tag.substring(6, this.tag.length());
    }

    public List<String> getTags() {
        return List.of(
                this.tag.replace(" ", "")
                        .split(",")
        );
    }
}
