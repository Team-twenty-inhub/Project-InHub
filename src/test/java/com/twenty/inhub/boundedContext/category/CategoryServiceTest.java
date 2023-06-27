package com.twenty.inhub.boundedContext.category;


import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired CategoryService categoryService;

    @Test
    @DisplayName("카테고리 생성")
    void no1() {
        CreateCategoryForm form = new CreateCategoryForm("category1", "about1");
        RsData<Category> categoryRs = categoryService.create(form);
        Category category = categoryRs.getData();

        assertThat(categoryRs.isSuccess()).isTrue();

        assertThat(category.getName()).isEqualTo("category1");
        assertThat(category.getAbout()).isEqualTo("about1");
    }

    @Test
    @DisplayName("카테고리 이름 중복 금지")
    void no2() {
        CreateCategoryForm form1 = new CreateCategoryForm("category1", "about1");
        categoryService.create(form1);

        CreateCategoryForm form2 = new CreateCategoryForm("category1", "about2");
        RsData<Category> categoryRs = categoryService.create(form2);

        assertThat(categoryRs.isFail()).isTrue();
    }
}