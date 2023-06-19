package com.twenty.inhub.boundedContext.book.controller.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookCreateForm {

    private String name;
    private String about;


    public BookCreateForm(String name, String about) {
        this.name = name;
        this.about = about;
    }
}
