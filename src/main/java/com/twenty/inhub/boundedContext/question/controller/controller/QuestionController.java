package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.controller.form.UpdateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Choice;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.question.entity.Tag;
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
        model.addAttribute("categoryId", id);
        model.addAttribute("mcq", MCQ);

        log.info("문제 생성폼 응답 확인");
        return "usr/question/top/create";
    }

    //-- 문제 타입 선택 폼 --//
    @GetMapping("/select/{id}")
    @PreAuthorize("isAuthenticated()")
    public String select(
            @PathVariable Long id,
            CreateQuestionForm form
    ) {
        log.info("문제 타입 선택 폼 요청 확인");
        form.setCategoryId(id);
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

    //-- name, content, choice, tag update 폼 --//
    @GetMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updateForm(
            UpdateQuestionForm form,
            @PathVariable Long id,
            Model model
    ) {
        log.info("Question update 폼 요청 확인 question id = {}", id);
        RsData<Question> questionRs = questionService.findById(id);

        if (questionRs.isFail()) {
            log.info("존재하지 않는 id / msg = {}", questionRs.getMsg());
            return rq.historyBack(questionRs.getMsg());
        }

        Member member = rq.getMember();
        Question question = questionRs.getData();
        if (member.getId() != question.getMember().getId()) {
            log.info("작성자가 다름 member id = {} / question's member id = {}", member.getId(), question.getMember().getId());
            return rq.historyBack("수정 권한이 없습니다.");
        }

        form.setForm(question.getName(), question.getContent(), question.getChoiceList(), question.getTags());

        model.addAttribute("question", question);
        model.addAttribute("mcq", MCQ);

        log.info("question 수정 폼 응답 완료");
        return "usr/question/top/update";
    }

    //-- name, content, choice, tag update 처리 --//
    @PostMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String update(
            UpdateQuestionForm form,
            @PathVariable Long id,
            Model model
    ) {
        log.info("Question update 요청 확인 question id = {}", id);
        RsData<Question> questionRs = questionService.findById(id);

        if (questionRs.isFail()) {
            log.info("존재하지 않는 id / msg = {}", questionRs.getMsg());
            return rq.historyBack(questionRs.getMsg());
        }

        Member member = rq.getMember();
        Question question = questionRs.getData();
        if (member.getId() != question.getMember().getId()) {
            log.info("작성자가 다름 member id = {} / question's member id = {}", member.getId(), question.getMember().getId());
            return rq.historyBack("수정 권한이 없습니다.");
        }

        RsData<Question> updateRs = questionService.update(question, form);

        if (updateRs.isFail()) {
            log.info("문제 수정 실패 msg = {}", updateRs.getMsg());
            return rq.historyBack(updateRs.getMsg());
        }

        log.info("question 수정 완료");
        return rq.redirectWithMsg("/question/detail/" + id, updateRs.getMsg());
    }

    //-- delete question --//
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id) {
        log.info("question 삭제 요청 확인 id = {}", id);

        RsData<Question> questionRs = questionService.findById(id);

        if (questionRs.isFail()) {
            log.info("존재하지 않는 id / msg = {}", questionRs.getMsg());
            return rq.historyBack(questionRs.getMsg());
        }

        Question question = questionRs.getData();
        Long categoryId = question.getCategory().getId();
        Member member = rq.getMember();

        if (question.getMember().getId() != member.getId()) {
            log.info("작성자가 다름 member id = {} / question's member id = {}", member.getId(), question.getMember().getId());
            return rq.historyBack("삭제 권한이 없습니다.");
        }

        RsData delete = questionService.delete(question);
        log.info("question 삭제 완료");
        return rq.redirectWithMsg("/question/list/" + categoryId, delete.getMsg());
    }
}