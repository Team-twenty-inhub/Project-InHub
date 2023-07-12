package com.twenty.inhub.boundedContext.book.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.service.BookService;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;

@Slf4j
@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final QuestionService questionService;
    private final MemberService memberService;
    private final Rq rq;


    //-- Search main --//
    @GetMapping("/main")
    public String main(
            SearchForm searchForm,
            Model model
    ) {
        log.info("search main page 요청 확인");

        int random = bookService.random(2);
        List<Book> books = bookService.findRandomBooks(random, 9);

        model.addAttribute("books", books);
        log.info("search main page 응답 완료");
        return "usr/search/top/search";
    }

    //-- search book by name --//
    @GetMapping("/book/name")
    public String bookName(
            @RequestParam(defaultValue = "0") int page,
            SearchForm form,
            Model model
    ) {
        log.info("name 으로 book 검색 요청 확인 page = {}", page);

        form.setCodePage(0, page);
        PageResForm<Book> books = bookService.findByInput(form);

        model.addAttribute("books", books);
        log.info("book 검색 결과 응답 완료 page = {} / total count = {}", books.getPage(), books.getCount());
        return "usr/search/top/book";
    }

    //-- search category --//
    @GetMapping("/category")
    public String category(
            @RequestParam(defaultValue = "0") int page,
            SearchForm form,
            Model model
    ) {
        log.info("category 검색 요청 확인 Page = {}", page);

        form.setCodePage(1, page);
        PageResForm<Category> categories = categoryService.findByInput(form);

        model.addAttribute("categories", categories);
        log.info("category 검색 결과 요청 완료 page = {} / total count = {}", categories.getPage(), categories.getCount());
        return "usr/search/top/category";
    }

    //-- search question --//
    @GetMapping("/question")
    public String question(
            @RequestParam(defaultValue = "0") int page,
            SearchForm form,
            Model model
    ) {
        log.info("question 검색 요청 확인 Page = {}", page);

        form.setCodePage(2, page);
        PageResForm<Question> questions = questionService.findByInput(form);

        model.addAttribute("questions", questions);
        model.addAttribute("mcq", MCQ);
        log.info("question 검색 결과 요청 완료 page = {} / total count = {}", questions.getPage(), questions.getCount());
        return "usr/search/top/question";
    }

    //-- search question --//
    @GetMapping("/category/{id}")
    public String categoryOfQuestion(
            @RequestParam(defaultValue = "0") int page,
            @PathVariable Long id,
            SearchForm form,
            Model model
    ) {
        log.info("category 별 question 검색 요청 확인 category id = {} / Page = {}", id, page);

        RsData<Category> categoryRs = categoryService.findById(id);

        if (categoryRs.isFail()) {
            log.info("category 조회 실패 msg = {}", categoryRs.getMsg());
            return rq.historyBack(categoryRs.getMsg());
        }

        Category category = categoryRs.getData();

        form.setCodePage(2, page);
        form.setInput(category.getName());
        PageResForm<Question> questions = questionService.findByInput(form);

        model.addAttribute("questions", questions);
        model.addAttribute("mcq", MCQ);
        log.info("category 별 question 검색 결과 요청 완료 page = {} / total count = {}", questions.getPage(), questions.getCount());
        return "usr/search/top/question";
    }

    //-- search member --//
    @GetMapping("/member")
    public String member(
            @RequestParam(defaultValue = "") String input,
            @RequestParam(defaultValue = "0") int page,
            SearchForm form,
            Model model
    ) {
        log.info("회원 검색 페이지 요청 확인 input = {}", input);

        form.setCodePage(4, page);

        Page<Member> memberList = memberService.getMemberList(page, input, "nickname");
        model.addAttribute("memberList", memberList);

        log.info("회원 검색 페이지 응답 완료");
        return "usr/search/top/member";
    }
}
