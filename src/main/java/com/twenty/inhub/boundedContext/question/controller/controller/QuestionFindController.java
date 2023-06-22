package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.question.controller.form.CreateAnswerForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateFunctionForm;
import com.twenty.inhub.boundedContext.question.controller.form.QuestionSearchForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import com.twenty.inhub.boundedContext.underline.Underline;
import com.twenty.inhub.boundedContext.underline.dto.UnderlineCreateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.twenty.inhub.boundedContext.member.entity.MemberRole.ADMIN;
import static com.twenty.inhub.boundedContext.member.entity.MemberRole.JUNIOR;
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
    public String list(
            @PathVariable Long id,
            QuestionSearchForm form,
            Model model
    ) {
        log.info("문제 목록 요청 확인 category id = {}", id);

        RsData<Category> categoryRs = categoryService.findById(id);

        if (categoryRs.isFail()) {
            log.info("조회 실패 msg = {}", categoryRs.getMsg());
            return rq.historyBack(categoryRs.getMsg());
        }

        Category category = categoryRs.getData();
        model.addAttribute("category", category);
        model.addAttribute("role", JUNIOR);
        model.addAttribute("mcq", MCQ);
        log.info("문제 목록 응답 완료 category id = {}", id);
        return "usr/question/top/list";
    }

    //-- 문제 검색 --//
    @GetMapping("/search")
    public String search(QuestionSearchForm form, Model model) {
        log.info("문제 검색 요청 확인 input = {}", form.getTag());

//        if (form.getSelect() == 1) {
//            List<Underline> underlines = rq.getMember().getUnderlines();
//            form.setUnderlines(underlines);
//        }

        List<Question> questions = questionService.findByInput(form);
        model.addAttribute("questions", questions);
        model.addAttribute("role", ADMIN);
        model.addAttribute("mcq", MCQ);

        log.info("검색한 문제 응답 완료");
        return "usr/question/top/search";
    }

    //-- 문제 상세 페이지 --//
    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable Long id,
            UnderlineCreateForm form,
            Model model
    ) {
        log.info("문제 상세페이지 요청 확인 question id = {}", id);

        RsData<Question> questionRs = questionService.findById(id);

        if (questionRs.isFail()) {
            log.info("조회 실패 msg = {}", questionRs.getMsg());
            return rq.historyBack(questionRs.getMsg());
        }

        Question question = questionRs.getData();
        AnswerCheck check = question.getAnswerCheck();
        List<Book> books = rq.getMember().getBooks();

        model.addAttribute("question", question);
        model.addAttribute("books", books);
        model.addAttribute("check", check);
        model.addAttribute("mcq", MCQ);

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
        model.addAttribute("mcq", MCQ);

        log.info("문제 설정폼 응답 완료");
        return "usr/question/top/function";
    }

    //-- 랜덤 문제 리스트 생성 --//
    @GetMapping("/playlist")
    @PreAuthorize("isAuthenticated()")
    public String playlist(CreateFunctionForm form, Model model) {
        log.info("문제 리스트 생성 요청 확인 question count = {}", form.getCount());

        List<Long> playlist = questionService.getPlaylist(form);
        rq.getSession().setAttribute("playlist", playlist);

        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");
        if (answerList != null) answerList.clear();

        model.addAttribute("mcq", MCQ);

        log.info("랜덤 문제 응답 완료 question count = {}", playlist.size());
        return "usr/question/top/playlist";
    }

    //-- 랜덤 문제 실행 --//
    @GetMapping("/play")
    @PreAuthorize("isAuthenticated()")
    public String play(
            @RequestParam(defaultValue = "0") int page,
            UnderlineCreateForm underlineCreateForm,
            CreateAnswerForm form,
            Model model
    ) {
        log.info("문제 리스트 실행 요청 확인 page = {}", page);

        List<Long> playlist = (List<Long>) rq.getSession().getAttribute("playlist");
        if (playlist == null) {
            log.info("플레이 리스트가 없습니다.");
            return rq.redirectWithMsg("/question/function", "문제 설정을 먼저 해주세요.");
        }

        List<Question> questions = questionService.findByIdList(playlist);
        if (page == questions.size()) {
            log.info("모든 문제 해결 완료");
            return rq.redirectWithMsg("/answer/list", "문제 제출 완료");
        }

        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");

        if (answerList == null)
            answerList = new ArrayList<>();
        else if (answerList.size() > page)
            form.setContent(answerList.get(page).getContent());

        model.addAttribute("size", questions.size() - 1);
        model.addAttribute("books", rq.getMember().getBooks());
        model.addAttribute("question", questions.get(page));
        model.addAttribute("page", page);
        model.addAttribute("mcq", MCQ);

        log.info("문제 리스트 실행 id = {}", questions.get(page).getId());
        return "usr/question/top/play";
    }
}
