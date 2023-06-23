package com.twenty.inhub.boundedContext.book.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.service.BookService;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import com.twenty.inhub.boundedContext.underline.UnderlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final QuestionService questionService;
    private final UnderlineService underlineService;
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

        form.setCodePage(0,page);
        PageResForm<Book> books = bookService.findContainName(form);

        model.addAttribute("books", books);
        log.info("book 검색 결과 응답 완료 page = {} / total count = {}", books.getPage(), books.getCount());
        return "usr/search/top/book";
    }

    //-- search book by tag --//
    @GetMapping("/book/tag")
    public String bookTag(
            @RequestParam(defaultValue = "0") int page,
            SearchForm form,
            Model model
    ) {
        log.info("tag 로 book 검색 요청 확인 page = {}", page);

        form.setCodePage(1,page);
        PageResForm<Book> books = bookService.findContainName(form);

        model.addAttribute("books", books);
        log.info("book 검색 결과 응답 완료 page = {} / total count = {}", books.getPage(), books.getCount());
        return "usr/search/top/bookTage";
    }
}
