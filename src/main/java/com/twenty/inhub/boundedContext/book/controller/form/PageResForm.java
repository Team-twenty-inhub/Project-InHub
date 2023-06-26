package com.twenty.inhub.boundedContext.book.controller.form;


import lombok.Data;

import java.util.List;

@Data
public class PageResForm<T> {

    private List<T> contents;
    private int page;
    private Long count;

    public PageResForm(List<T> contents, int page, Long count) {
        this.contents = contents;
        this.page = page;
        this.count = count;
    }
}
