package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.question.controller.form.CreateFunctionForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;

@Slf4j
@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionFindController {

    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final Rq rq;


    //-- 카테고리 별 문제 목록 조회 --//
    @GetMapping("/list/{id}")
    public String list(@PathVariable Long id, Model model) {
        log.info("문제 목록 요청 확인 category id = {}", id);

        RsData<Category> categoryRs = categoryService.findById(id);

        if (categoryRs.isFail()) {
            log.info("조회 실패 msg = {}", categoryRs.getMsg());
            return rq.historyBack(categoryRs.getMsg());
        }

        Category category = categoryRs.getData();
        model.addAttribute("role", MemberRole.ADMIN);
        model.addAttribute("category", category);
        log.info("문제 목록 응답 완료 category id = {}", id);
        return "usr/question/top/list";
    }

    //-- 문제 상세 페이지 --//
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("문제 상세페이지 요청 확인 question id = {}", id);

        RsData<Question> questionRs = questionService.findById(id);

        if (questionRs.isFail()) {
            log.info("조회 실패 msg = {}", questionRs.getMsg());
            return rq.historyBack(questionRs.getMsg());
        }

        Question question = questionRs.getData();
        model.addAttribute("mcq", MCQ);
        model.addAttribute("question", question);
        log.info("문제 상세페이지 응답 완료 category id = {}", id);
        return "usr/question/top/detail";
    }

    //-- 문제 풀기 설정폼 --//
    @GetMapping("/function")
    @PreAuthorize("isAuthenticated()")
    public String function(CreateFunctionForm form, Model model) {
        log.info("문제 풀기 설정폼 요청 확인");

        List<Category> categories = categoryService.findAll();
        List<QuestionType> types = questionService.findQuestionType();
        List<Integer> difficulties = questionService.findDifficultyList();

        model.addAttribute("difficulties", difficulties);
        model.addAttribute("categories", categories);
        model.addAttribute("types", types);

        log.info("문제 설정폼 응답 완료");
        return "usr/question/top/function";
    }

    //-- 문제 풀기 실행 --//
    @GetMapping("/play")
    @PreAuthorize("isAuthenticated()")
    public String play(CreateFunctionForm form, Model model) {
        log.info("문제 풀기 요청 확인");

        RsData<Question> questionRs = questionService.playQuestion(form);

        return rq.historyBack("good");
    }
}
