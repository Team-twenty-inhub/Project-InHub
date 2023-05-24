package com.twenty.inhub.boundedContext.question.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.question.controller.form.CreateChoiceForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateSubjectiveForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuestionServiceTest {

    @Autowired QuestionService questionService;
    @Autowired CategoryService categoryService;


    @Test
    void 주관식_문제_생성() {
        Member member = member();
        Category category = category("category");
        CreateSubjectiveForm form = new CreateSubjectiveForm("주관식", "내용", "태그1, 태그2");
        RsData<Question> questionRs = questionService.create(form, member, category);
        Question question = questionRs.getData();

        assertThat(questionRs.isSuccess()).isTrue();

        assertThat(question.getType()).isEqualTo(QuestionType.SUBJECTIVE);
        assertThat(question.getName()).isEqualTo("주관식");
        assertThat(question.getContent()).isEqualTo("내용");


        // 태그 검증 //
        List<String> tags = question.getTags();

        assertThat(tags.size()).isEqualTo(2);
        assertThat(tags.get(0)).isEqualTo("태그1");
        assertThat(tags.get(1)).isEqualTo("태그2");
    }

    @Test
    void 객관식_문제_생성() {
        Member member = member();
        Category category = category("category");
        CreateChoiceForm form = new CreateChoiceForm("객관식", "내용", "지문1#지문2#지문3#지문4", "태그");
        RsData<Question> questionRs = questionService.create(form, member, category);
        Question question = questionRs.getData();

        assertThat(questionRs.isSuccess()).isTrue();

        assertThat(question.getType()).isEqualTo(QuestionType.CHOICE);
        assertThat(question.getName()).isEqualTo("객관식");


        // 객관식 선택지 검증 //
        List<String> choice = question.getChoiceList();

        assertThat(choice.size()).isEqualTo(4);
        assertThat(choice.get(0)).isEqualTo("지문1");
        assertThat(choice.get(3)).isEqualTo("지문4");
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