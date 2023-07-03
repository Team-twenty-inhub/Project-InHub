package com.twenty.inhub.boundedContext.note;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteSendForm {

    private String title;
    private String content;
}
