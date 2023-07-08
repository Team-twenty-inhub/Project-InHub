package com.twenty.inhub.boundedContext.question.controller.form;

import com.twenty.inhub.boundedContext.question.entity.Choice;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.Tag;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateQuestionForm {

    private String name;
    private String content;
    private List<String> choiceList = new ArrayList<>();

    private String tag;

    public void setForm(Question question, List<Tag> tags) {
        this.name = question.getName();
        this.content = question.getContent();

        List<Choice> choiceList = question.getChoiceList();

        for (Choice choice : choiceList)
            this.choiceList.add(choice.getChoice());

        if (tag == null) tag = "";

        for (Tag tag : tags)
            this.tag = this.tag + ", " + tag.getTag();

        if (!tag.equals(""))
            this.tag = this.tag.substring(2, this.tag.length());
    }

    public List<String> getTags() {
        return List.of(
                this.tag.replace(" ", "")
                        .split(",")
        );
    }
}
