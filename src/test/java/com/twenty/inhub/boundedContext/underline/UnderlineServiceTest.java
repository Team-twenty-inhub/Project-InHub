package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class UnderlineServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private UnderlineService underlineService;
    @Autowired private CategoryService categoryService;
    @Autowired private QuestionService questionService;


    @Test
    void Member_Category_로_Underline_조회() {
        Member member = member("member");
        Member member2 = member("member2");
        Category category1 = category("category1");
        Category category2 = category("category2");
        for (int i = 0; i < 5; i++) {
            Question question1 = question("Q" + i, category1, member);
            Question question2 = question("Q" + i, category2, member);
            if (i%2 == 0) underline(question1, member);
            else underline(question2, member);
            underline(question1, member2);
        }

        List<Question> all = questionService.findAll();
        assertThat(all.size()).isEqualTo(10);
        assertThat(member.getUnderlines().size()).isEqualTo(5);

        List<Underline> underline1 = underlineService.findByCategory(member, category1);
        List<Underline> underline2 = underlineService.findByCategory(member, category2);
        List<Underline> underline3 = underlineService.findByCategory(member2, category1);

        assertThat(underline1.size()).isEqualTo(3);
        assertThat(underline2.size()).isEqualTo(2);
        assertThat(underline3.size()).isEqualTo(5);
    }

    private void underline(Question question, Member member) {
        underlineService.create("about", member, question);
    }


    private Question question(String name, Category category, Member member) {
        return questionService.create(new CreateQuestionForm(name, name, "", null, null, QuestionType.SAQ), member, category).getData();
    }

    private Category category(String name) {
        return categoryService.create(new CreateCategoryForm(name, "")).getData();
    }

    private Member member(String name) {
        Member member = memberService.create(name, "1234").getData();
        member.setRole(MemberRole.ADMIN);
        return member;
    }
}