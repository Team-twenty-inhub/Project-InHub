package com.twenty.inhub.boundedContext.question.service;

import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class QuestionServiceTest {

    @Autowired QuestionService questionService;
    @Autowired CategoryService categoryService;


    @Test
    void 주관식_문제_생성() {
        Member member = member();
        Category category = category("category");
    }

    @Test
    void 객관식_문제_생성() {
        Member member = member();
        Category category = category("category");
    }




    private Member member() {
        return Member.builder()
                .role(MemberRole.SENIOR)
                .build();
    }

    private Category category(String name) {
      return categoryService.create(new CreateCategoryForm(name, "about1")).getData();
    }

}