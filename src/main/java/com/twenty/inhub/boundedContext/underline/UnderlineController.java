package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;

@Slf4j
@Controller
@RequestMapping("/underline")
@RequiredArgsConstructor
public class UnderlineController {

    private final UnderlineService underlineService;
    private final MemberService memberService;
    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final Rq rq;


    //-- 밑줄 긋기 생성 --//
    @PostMapping("/create/{member}/{question}/{page}")
    @PreAuthorize("isAuthenticated()")
    public String create(
            String about,
            @PathVariable("member") Long memberId,
            @PathVariable("question") Long questionId,
            @PathVariable int page
    ) {
        log.info("밑줄 긋기 생성 요청 확인 question id = {}", questionId);

        Member member = memberService.findById(memberId).get();
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
    @PreAuthorize("isAuthenticated()")
    public String categoryList(Model model) {
        log.info("밑줄 문제 카테고리 목록 요청 확인");

        List<Underline> underlines = rq.getMember().getUnderlines();
        Set<Category> categories = new HashSet<>();
        for (Underline underline : underlines)
            categories.add(underline.getQuestion().getCategory());

        model.addAttribute("categories", categories);

        log.info("밑줄 문제 카테고리 목록 응답 완료 count = {}", underlines.size());
        return "usr/underline/top/category";
    }

    //-- 밑줄 문제 목로 --//
    @GetMapping("/list/{id}")
    @PreAuthorize("isAuthenticated()")
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
}
