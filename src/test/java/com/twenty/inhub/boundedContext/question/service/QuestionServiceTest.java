package com.twenty.inhub.boundedContext.question.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.twenty.inhub.boundedContext.member.entity.MemberRole.ADMIN;
import static com.twenty.inhub.boundedContext.question.entity.QuestionType.SAQ;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuestionServiceTest {

    @Autowired QuestionService questionService;
    @Autowired CategoryService categoryService;


    @Test
    void 문제_생성() {
        Member member = member();
        Category category = category("category");
        List<String> tags = createList("태그1", "태그2", "태그3");
        List<String> choice = createList("선택지1", "선택지2", "선택지3");
        CreateQuestionForm form = new CreateQuestionForm("주관식", "설명", tags, choice, category.getId(), SAQ);

        RsData<Question> questionRs = questionService.create(form, member, category);
        Question question = questionRs.getData();

        assertThat(questionRs.isSuccess()).isTrue();
        assertThat(question.getType()).isEqualTo(SAQ);
        assertThat(question.getName()).isEqualTo("주관식");
        assertThat(question.getContent()).isEqualTo("설명");
        assertThat(question.getTags().size()).isEqualTo(3);
        assertThat(question.getChoiceList().get(0).getChoice()).isEqualTo("선택지1");
    }


    private List<String> createList(String s1, String s2, String s3) {
        List<String> list = new ArrayList<>();
        list.add(s1); list.add(s2); list.add(s3);
        return list;
    }

    private Member member() {
        return Member.builder()
                .role(ADMIN)
                .build();
    }

    private Category category(String name) {
        return categoryService.create(new CreateCategoryForm(name, "about1")).getData();
    }

}