package com.twenty.inhub.boundedContext.book.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.controller.form.BookUpdateForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.service.BookService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;

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
    public String createForm(
            BookCreateForm form,
            Model model
    ) {
        Member member = rq.getMember();
        log.info("book 생성폼 요청 확인 member id = {}", member.getId());

        model.addAttribute("num", bookService.random(10));

        log.info("book 생성폼 응답 완료 member id = {}", member.getId());
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

        log.info("book 생성 완료 book id = {}", bookRs.getData().getId());
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

        if (bookRs.isFail()) {
            log.info("Book 조회 실패 msg = {}", bookRs.getMsg());
            return rq.historyBack(bookRs.getMsg());
        }

        RsData<List<Long>> playlistRs = bookService.getPlayList(bookRs.getData());

        if (playlistRs.isFail()) {
            log.info("playlist 생성 실패 msg = {} / size = {}", playlistRs.getMsg(), playlistRs.getData().size());
            return rq.historyBack("3문제 이상 수록된 문제집 부터 풀어볼 수 있습니다.");
        }

        rq.getSession().setAttribute("playlist", playlistRs.getData());
        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");
        if (answerList != null) answerList.clear();

        rq.getSession().setAttribute("book", id);

        model.addAttribute("book", bookRs.getData());
        log.info("book playlist 생성 완료 book id = {}/ question count = {}", id, playlistRs.getData().size());
        return "usr/book/top/playlist";
    }

    //-- 문제집 수정 폼--//
    @GetMapping("/update/{id}")
    public String updateForm(
            @PathVariable Long id,
            BookUpdateForm form,
            Model model
    ) {
        log.info("Book 수정 폼 요청 확인 book id = {}", id);

        RsData<Book> bookRs = bookService.findById(id);
        if (bookRs.isFail()) {
            log.info("book 조회 실패 mst = {}", bookRs.getMsg());
            return rq.historyBack(bookRs.getMsg());
        }

        Member member = rq.getMember();
        Book book = bookRs.getData();
        if (book.getMember() != member) {
            log.info("수정 권한 없음");
            return rq.historyBack("수정 권한이 없습니다.");
        }
        form.setting(book.getImg(), book.getName(), book.getAbout(), book.getTagList());

        model.addAttribute("books", member.getBooks());
        model.addAttribute("book", book);
        model.addAttribute("mcq", MCQ);
        log.info("book 수정 폼 응답 완료 book id = {}", id);
        return "usr/book/top/update";
    }

    //-- book 수정 --//
    @PostMapping("/update/{id}")
    public String update(
            @PathVariable Long id,
            BookUpdateForm form
    ) {
        log.info("Book 수정 요청 확인 book id = {}", id);

        RsData<Book> findBookRs = bookService.findById(id);
        if (findBookRs.isFail()) {
            log.info("book 조회 실패 msg = {}", findBookRs.getMsg());
            return rq.historyBack(findBookRs.getMsg());
        }

        if (rq.getMember() != findBookRs.getData().getMember()) {
            log.info("수정 권한 없음");
            return rq.historyBack("수정 권한이 없습니다.");
        }

        RsData<Book> bookRs = bookService.update(findBookRs.getData(), form);
        if (bookRs.isFail()) {
            log.info("book update 실패 msg = {}", bookRs.getMsg());
            return rq.historyBack(bookRs.getMsg());
        }

        log.info("book 수정 완료 book id = {}", bookRs.getData().getId());
        return rq.redirectWithMsg("/underline/book/" + id, bookRs.getMsg());
    }
}
