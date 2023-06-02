package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.twenty.inhub.boundedContext.member.entity.MemberRole.JUNIOR;
import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;

@Slf4j
@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final Rq rq;


    //-- 문제 생성폼 --//
    @GetMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createForm(
            @PathVariable Long id,
            CreateQuestionForm form,
            Model model
    ) {
        log.info("문제 생성폼 요청 확인");

        if (rq.getMember().getRole() == JUNIOR) {
            log.info("접근 권한 없음 role = {}", rq.getMember().getRole());
            return rq.historyBack("접근 권한이 없습니다.");
        }


        if (form.getType() == null) form.setType(MCQ);
        form.setCategoryId(id);

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("mcq", MCQ);

        log.info("문제 생성폼 응답 확인");
        return "usr/question/top/create";
    }

    //-- 문제 타입 선택 폼 --//
    @GetMapping("/select")
    @PreAuthorize("isAuthenticated()")
    public String select(CreateQuestionForm form) {
        log.info("문제 타입 선택 폼 요청 확인");
        return "usr/question/top/select";
    }

    //-- 문제 생성 처리 --//
    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String create(
            @PathVariable Long id,
            CreateQuestionForm form
    ) {
        log.info("문제 생성 처리 확인 category id = {}", id);

        Member member = rq.getMember();
        if (member.getRole() == JUNIOR) {
            log.info("접근 권한 없음 role = {}", member.getRole());
            return rq.historyBack("접근 권한이 없습니다.");
        }

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

        if (form.getType().equals(QuestionType.SAQ))
            return rq.redirectWithMsg("/answer/check/create/%s".formatted(questionRs.getData().getId()), "서술형 문제 등록 완료");

        return rq.redirectWithMsg("/answer/mcq/create/%s".formatted(questionRs.getData().getId()), "객관식 문제 등록 완료");
    }
}