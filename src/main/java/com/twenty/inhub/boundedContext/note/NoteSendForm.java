package com.twenty.inhub.boundedContext.note;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteSendForm {

    private String receiver;
    private String title;
    private String content;
}
