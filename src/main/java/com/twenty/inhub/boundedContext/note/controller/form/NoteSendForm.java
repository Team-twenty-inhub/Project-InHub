package com.twenty.inhub.boundedContext.note.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteSendForm {

    @NotBlank
    private String receiver;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
