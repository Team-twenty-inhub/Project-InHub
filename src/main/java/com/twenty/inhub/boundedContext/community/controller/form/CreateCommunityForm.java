package com.twenty.inhub.boundedContext.community.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommunityForm {

    private String title;
    private String content;
    private String boardName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
