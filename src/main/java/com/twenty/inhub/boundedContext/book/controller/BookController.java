package com.twenty.inhub.boundedContext.book.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.service.BookService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.controller.form.QuestionSearchForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class BookController {

    private final BookService bookService;
    private final Rq rq;


    //-- book 생성 폼 --//
    @GetMapping("/create")
    public String createForm(BookCreateForm form) {
        Member member = rq.getMember();
        log.info("book 생성폼 요청 확인 member id = {}", member.getId());

        return "usr/book/top/create";
    }


    //-- book 생성 처리 --//
    @PostMapping("/create")
    public String create(BookCreateForm form) {
        Member member = rq.getMember();
        log.info("book 생성 요청 확인 member id = {}", member.getId());

        RsData<Book> bookRs = bookService.create(form, member);

        if (bookRs.isFail()) {
            log.info("book 생성 실패");
            return rq.historyBack(bookRs.getMsg());
        }

        return rq.redirectWithMsg("/", bookRs.getMsg());
    }

    //-- book list --//
    @GetMapping("/list")
    public String list(
            QuestionSearchForm form,
            Model model
    ) {
        Member member = rq.getMember();
        log.info("book 목록 요청 확인 member id = {}", member.getId());


        List<Book> books = bookService.findByMember(member);
        form.setSelect(1);

        model.addAttribute("books", books);
        log.info("book 목록 요청 완료 book size = {}", books.size());
        return "usr/book/top/list";
    }
}
