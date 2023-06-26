package com.twenty.inhub.boundedContext.underline.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnderlineCreateForm {

    private Long bookId;
    private String about;

    public UnderlineCreateForm(Long bookId, String about) {
        this.bookId = bookId;
        this.about = about;
    }
}
