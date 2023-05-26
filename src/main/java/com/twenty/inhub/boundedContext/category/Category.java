package com.twenty.inhub.boundedContext.category;

import com.twenty.inhub.base.entity.BaseEntity;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Category extends BaseEntity {

    private String name;
    private String about;

    @Builder.Default
    @OneToMany(mappedBy = "category")
    List<Question> questions = new ArrayList<>();


    //-- create method --//
    protected static Category crateCategory(CreateCategoryForm form) {
        return Category.builder()
                .name(form.getName())
                .about(form.getAbout())
                .build();
    }
}
