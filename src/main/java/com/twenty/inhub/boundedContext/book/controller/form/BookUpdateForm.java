package com.twenty.inhub.boundedContext.book.controller.form;

import com.twenty.inhub.boundedContext.book.entity.BookTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateForm {

    private String img;
    private String name;
    private String about;
    private String tag;
    private MultipartFile updateImg;
    private Long term;
    private List<Long> underlines;

    public void setting(String img, String name, String about, List<BookTag> tags) {
        this.img = img;
        this.name = name;
        this.about = about;
        this.tag = "";

        for (BookTag tag : tags)
            this.tag += ", " + tag.getTag();

        tag = tag.substring(2);
    }

    public List<String> getTags() {
        return List.of(
                this.tag.replace(" ", "")
                        .split(",")
        );
    }
}
