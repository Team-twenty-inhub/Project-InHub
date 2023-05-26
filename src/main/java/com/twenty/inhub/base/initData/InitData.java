package com.twenty.inhub.base.initData;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Choice;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;
import static com.twenty.inhub.boundedContext.question.entity.QuestionType.SAQ;

@Configuration
public class InitData {

    @Bean
    CommandLineRunner init(
            MemberService memberService,
            CategoryService categoryService,
            QuestionService questionService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member memberAdmin = memberService.create("admin", "1234").getData();


                //-- 카테고리 init data 추가 --//
                Category network = createCategory("네트워크");
                createCategory("운영체제");
                createCategory("데이터베이스");
                createCategory("알고리즘");
                createCategory("암호학/보안");
                createCategory("컴파일러");

                //-- 네트워크에 객관식 문제 추가 --//
                for (int i = 0; i <3; i++)
                    createMCQ(network, i + "번 문제", "내용내용");

                //-- 네트워크에 주관식 문제 추가 --//
                for (int i = 0; i <3; i++)
                    createSAQ(network, i + 3 + "번 문제", "내용 내용");
            }




            // 카테고리 생성 //
            private Category createCategory(String name) {
                return categoryService.create(new CreateCategoryForm(name, name + " 와 관련된 문제")).getData();
            }

            // 객관식 문제 생성 //
            private void createMCQ(Category category, String name, String content) {

                Member admin = memberService.findByUsername("admin").get();

                List<String> tags = new ArrayList<>();
                List<String> choice = new ArrayList<>();
                choice.add("1번 선택지");
                choice.add("2번 선택지");
                choice.add("3번 선택지");

                CreateQuestionForm form = new CreateQuestionForm(name, content, tags, choice, category.getId(), MCQ);
                questionService.create(form, admin, category);
            }

            // 주관식 문제 생성 //
            private void createSAQ(Category category, String name, String content) {

                Member admin = memberService.findByUsername("admin").get();

                List<String> tags = new ArrayList<>();
                List<String> choice = new ArrayList<>();
                choice.add("키키키");
                choice.add("워워워");
                choice.add("드드드");

                CreateQuestionForm form = new CreateQuestionForm(name, content, tags, choice, category.getId(), SAQ);
                questionService.create(form, admin, category);
            }
        };
    }
}
