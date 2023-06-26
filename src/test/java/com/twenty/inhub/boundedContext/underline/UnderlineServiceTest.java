package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.entity.Book;
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
import com.twenty.inhub.boundedContext.underline.dto.UnderlineCreateForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("문제집 얿을 때 기본 문제집 생성")
    void no1() {
        Member member = member();
        Category category = category("category");
        Question question = question("question", category, member);

        UnderlineCreateForm form = new UnderlineCreateForm(0L, "about");

        RsData<Underline> underlineRs = underlineService.create(form, question, member);

        assertThat(underlineRs.isSuccess()).isTrue();
        assertThat(underlineRs.getData().getAbout()).isEqualTo("about");

        Book book = underlineRs.getData().getBook();
        assertThat(book.getName()).isEqualTo("기본 문제집");
        assertThat(book.getAbout()).isEqualTo("밑줄 문제 모음집");
    }

    private Question question(String name, Category category, Member member) {
        return questionService.create(new CreateQuestionForm(name, name, "", null, null, QuestionType.SAQ), member, category).getData();
    }

    private Category category(String name) {
        return categoryService.create(new CreateCategoryForm(name, "")).getData();
    }

    private Member member() {
        Member member = memberService.create("admin", "1234").getData();
        member.setRole(MemberRole.ADMIN);
        return member;
    }
}