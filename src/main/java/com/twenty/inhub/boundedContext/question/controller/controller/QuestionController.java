package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.twenty.inhub.boundedContext.member.entity.MemberRole.ADMIN;

@Slf4j
@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final Rq rq;


    //-- 문제 생성폼 --//
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createForm(CreateQuestionForm form, Model model) {
        log.info("문제 생성폼 요청 확인");

        MemberRole role = rq.getMember().getRole();
        if (role != ADMIN) {
            log.info("접근 권한 없음 member rule = {}", role);
            return rq.historyBack("접근 권한이 없습니다.");
        }

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        log.info("문제 생성폼 응답 확인");
        return "usr/question/top/create";
    }

    //-- 문제 생성 처리 --//
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(CreateQuestionForm form) {
        log.info("문제 생성 처리 확인");

        Member member = rq.getMember();

        RsData<Category> categoryRs = categoryService.findById(form.getCategoryId());
        if (categoryRs.isFail()) {
            log.info("카테고리 조회 실패 msg = {}", categoryRs.getMsg());
            return rq.historyBack(categoryRs.getMsg());
        }

        RsData<Question> questionRs = questionService.create(form, member, categoryRs.getData());
        if (questionRs.isFail()) {
            log.info("question 생성 실패 msg = {}", questionRs.getMsg());
            return rq.historyBack(questionRs.getMsg());
        }

        log.info("question 생성 완료 id = {}", questionRs.getData().getId());
        return rq.redirectWithMsg("/", questionRs.getMsg());
    }

}