package com.twenty.inhub.base.initData;

import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class InitData {

    @Bean
    CommandLineRunner init(MemberService memberService, CategoryService categoryService) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member memberAdmin = memberService.create("admin", "1234").getData();


                //-- 카테고리 init data 추가 --//
                createCategory("네트워크");
                createCategory("운영체제");
                createCategory("데이터베이스");
                createCategory("알고리즘");
                createCategory("암호학/보안");
                createCategory("컴파일러");
            }





            private Category createCategory(String name) {
                return categoryService.create(new CreateCategoryForm(name, name + " 와 관련된 문제")).getData();
            }
        };
    }
}
