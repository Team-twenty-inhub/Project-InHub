package com.twenty.inhub.boundedContext.question.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.controller.form.CreateFunctionForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.twenty.inhub.boundedContext.member.entity.MemberRole.ADMIN;
import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;
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

    @Test
    void 랜덤_문제_조회하기() {
        Member member = member();
        List<Long> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Category category = category("cate" + i);
            categories.add(category.getId());
            for (int j = 0; j < 3; j++) {
                question("주관식" + j, category, SAQ, member);
                question("객관식" + j, category, MCQ, member);
            }
        }



        List<QuestionType> types = createType(SAQ);
        List<Integer> difficulties = createDif(0);
        categories.remove(4L);
//        new CreateFunctionForm()
//        questionService.playQuestion();
    }


    private List<QuestionType> createType(QuestionType type) {
        List<QuestionType> list = new ArrayList<>();
        list.add(type);
        return list;
    }

    private List<Integer> createDif(Integer i) {
        List<Integer> list = new ArrayList<>();
        list.add(i);
        return list;
    }

    private List<String> createList(String s1, String s2, String s3) {
        List<String> list = new ArrayList<>();
        list.add(s1); list.add(s2); list.add(s3);
        return list;
    }

    private void question(String name, Category category, QuestionType type, Member member) {
        List<String> list = new ArrayList<>();
        CreateQuestionForm form = new CreateQuestionForm(name, "content", list, list, category.getId(), type);
        questionService.create(form, member, category);
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