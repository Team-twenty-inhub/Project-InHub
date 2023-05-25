package com.twenty.inhub.boundedContext.category.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCategoryForm {

    @NotBlank
    private String name;
    private String about;
}