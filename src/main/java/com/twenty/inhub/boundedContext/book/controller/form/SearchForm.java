package com.twenty.inhub.boundedContext.book.controller.form;

import lombok.Data;

@Data
public class SearchForm {

    private int code;
    private String input;
    private int page;

    public void setCodePage(int code, int page) {
        this.code = code;
        this.page = page;
    }
}
