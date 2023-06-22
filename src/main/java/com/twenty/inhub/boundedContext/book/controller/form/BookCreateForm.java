package com.twenty.inhub.boundedContext.book.controller.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class BookCreateForm {

    private String name;
    private String about;
    private String tags;
    private MultipartFile img;


    public BookCreateForm(String name, String about) {
        this.name = name;
        this.about = about;
    }

    public BookCreateForm(String name, String about, String tags, MultipartFile img) {
        this.name = name;
        this.about = about;
        this.tags = tags;
        this.img = img;
    }

    public List<String> getTagList() {
        return List.of(
                this.tags.replace(" ", "")
                        .split(",")
        );
    }
}
