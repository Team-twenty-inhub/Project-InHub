package com.twenty.inhub.boundedContext.question.controller.form;

import com.twenty.inhub.boundedContext.question.entity.Choice;
import com.twenty.inhub.boundedContext.question.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class UpdateQuestionForm {

    private String name;
    private String content;
    private List<String> choiceList;

    private String tag;

    public void setForm(String name, String content, List<Choice> choiceList, List<Tag> tags) {
        this.name = name;
        this.content = content;

        for (Choice choice : choiceList)
            this.choiceList.add(choice.getChoice());

        for (Tag tag : tags)
            this.tag = this.tag + ", " + tag.getTag();

        this.tag = this.tag.substring(2, this.tag.length() - 1);
    }
}
