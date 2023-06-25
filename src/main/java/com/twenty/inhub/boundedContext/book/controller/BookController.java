package com.twenty.inhub.boundedContext.book.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.service.BookService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.controller.form.QuestionSearchForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "0") int page,
            SearchForm form,
            Model model
    ) {
        Member member = rq.getMember();
        log.info("book 목록 요청 확인 member id = {}", member.getId());

        form.setCodePage(0, page);
        List<Book> books = bookService.findByMember(member);

        model.addAttribute("books", books);
        log.info("book 목록 요청 완료 book size = {}", books.size());
        return "usr/book/top/list";
    }

    //-- 문제집 풀기 폼 --//
    @GetMapping("/playlist/{id}")
    @PreAuthorize("isAuthenticated()")
    public String playlist(
            @PathVariable Long id,
            Model model
    ) {
        log.info("문제집 풀기 폼 요청 확인 book id = {}", id);

        RsData<Book> bookRs = bookService.findById(id);

        if (bookRs.isFail()){
            log.info("Book 조회 실패 msg = {}", bookRs.getMsg());
            return rq.historyBack(bookRs.getMsg());
        }

        List<Long> playlist = bookService.getPlayList(bookRs.getData());

        if (playlist.size() > 3) {
            log.info("Playlist 가 3 보다 짧음 size = {}", playlist.size());
            return rq.historyBack("3문제 이상 수록된 문제집 부터 풀어볼 수 있습니다.");
        }

        rq.getSession().setAttribute("playlist", playlist);
        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");
        if (answerList != null) answerList.clear();

        log.info("book playlist 생성 완료 book id = {}/ question count = {}", id, playlist.size());
        return "usr/book/top/playlist";
    }
}
