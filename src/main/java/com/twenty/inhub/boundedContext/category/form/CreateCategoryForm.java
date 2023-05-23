package com.twenty.inhub.boundedContext.category.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCategoryForm {

    private String name;
    private String about;
}
