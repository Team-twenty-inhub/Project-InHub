package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;

@Slf4j
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/underline")
@RequiredArgsConstructor
public class UnderlineController {

    private final UnderlineService underlineService;
    private final MemberService memberService;
    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final Rq rq;


    //-- 밑줄 긋기 생성 --//
    @PostMapping("/create/{question}/{page}")
    public String create(
            String about,
            @PathVariable("question") Long questionId,
            @PathVariable int page
    ) {
        log.info("밑줄 긋기 생성 요청 확인 question id = {}", questionId);

        Member member = rq.getMember();
        RsData<Question> questionRs = questionService.findById(questionId);

        if (questionRs.isFail()) {
            log.info("존재하지 않는 문제 msg = {}", questionRs.getMsg());
            return rq.historyBack(questionRs.getMsg());
        }

        RsData<Underline> underlineRs = underlineService.create(about, member, questionRs.getData());

        if (underlineRs.isFail()) {
            log.info("밑줄 긋기 실패 msg = {}", underlineRs.getMsg());
            return rq.historyBack(underlineRs.getMsg());
        }

        log.info("밑줄 긋기 성공 id = {}", underlineRs.getData().getId());

        if (page == 1000)
            return rq.redirectWithMsg("/question/detail/" + questionId, underlineRs.getMsg());

        else
            return rq.redirectWithMsg("/question/play?page=" + page, underlineRs.getMsg());
    }

    //-- 밑줄 문제 카테고리 목록 --//
    @GetMapping("/category")
    public String categoryList(Model model) {
        log.info("밑줄 문제 카테고리 목록 요청 확인");

        // underline 의 카테고리별 count 를 어떻게하면 효율적으로 조회할 수 있을까?
        Member member = rq.getMember();
        List<Question> questions = member.getUnderlines().stream()
                .map(Underline::getQuestion)
                .collect(Collectors.toList());
        List<Category> categories = categoryService.findContainUnderline(member, questions);

        model.addAttribute("categories", categories);

        log.info("밑줄 문제 카테고리 목록 응답 완료 count = {}", categories.size());
        return "usr/underline/top/category";
    }

    //-- 밑줄 문제 목록 --//
    @GetMapping("/list/{id}")
    public String list(
            @PathVariable Long id,
            Model model
    ) {
        log.info("밑줄 문제 목록 요청 확인 category id = {}", id);

        RsData<Category> categoryRs = categoryService.findById(id);
        Member member = rq.getMember();

        if (categoryRs.isFail()) {
            log.info("조회 실패 msg = {}", categoryRs.getMsg());
            return rq.historyBack(categoryRs.getMsg());
        }
        Category category = categoryRs.getData();

        List<Question> questions = questionService
                .findByCategoryUnderline(category, member.getUnderlines());

        model.addAttribute("category", category);
        model.addAttribute("questions", questions);
        model.addAttribute("mcq", MCQ);

        log.info("밑줄 문제 목록 응답 완료 category id = {} / count = {}", id, questions.size());
        return "usr/underline/top/list";
    }

    //-- underline 문제 상세페이지 --//
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("밑줄 문제 상세페이지 요청 확인 question id = {}", id);

        RsData<Question> questionRs = questionService.findById(id);

        if (questionRs.isFail()) {
            log.info("조회 실패 msg = {}", questionRs.getMsg());
            return rq.historyBack(questionRs.getMsg());
        }
        Question question = questionRs.getData();
        AnswerCheck check = question.getAnswerCheck();

        RsData<Underline> underlineRs = underlineService.findByMemberQuestion(rq.getMember(), question);
        if (underlineRs.isFail()) {
            log.info("밑줄 목록 조회 실패 msg = {}", underlineRs.getMsg());
            return rq.historyBack(underlineRs.getMsg());
        }

        model.addAttribute("underline", underlineRs.getData());
        model.addAttribute("question", question);
        model.addAttribute("check", check);
        model.addAttribute("mcq", MCQ);

        log.info("문제 상세페이지 응답 완료 category id = {}", id);
        return "usr/underline/top/detail";
    }
}
