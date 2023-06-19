package com.twenty.inhub.boundedContext.book.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.service.BookService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final Rq rq;


    //-- book 생성 처리 --//
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
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
}
