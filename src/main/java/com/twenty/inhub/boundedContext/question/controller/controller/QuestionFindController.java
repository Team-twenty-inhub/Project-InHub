package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("question", question);
        log.info("문제 상세페이지 응답 완료 category id = {}", id);
        return "usr/question/top/detail";
    }
}
