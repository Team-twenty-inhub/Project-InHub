package com.twenty.inhub.boundedContext.book.controller.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class BookCreateForm {

    private String name;
    private String about;
    private String tags;


    public BookCreateForm(String name, String about) {
        this.name = name;
        this.about = about;
    }

    public List<String> getTags() {
        return List.of(
                this.tags.replace(" ", "")
                        .split(",")
        );
    }
}
