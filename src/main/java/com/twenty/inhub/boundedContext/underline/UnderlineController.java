package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.service.BookService;
import com.twenty.inhub.boundedContext.question.controller.form.QuestionSearchForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import com.twenty.inhub.boundedContext.underline.dto.UnderlineCreateForm;
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

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;

@Slf4j
@Controller
@RequestMapping("/underline")
@RequiredArgsConstructor
public class UnderlineController {

    private final UnderlineService underlineService;
    private final QuestionService questionService;
    private final BookService bookService;
    private final Rq rq;


    //-- 밑줄 긋기 생성 --//
    @PostMapping("/create/{question}/{page}")
    @PreAuthorize("isAuthenticated()")
    public String create(
            UnderlineCreateForm form,
            @PathVariable("question") Long questionId,
            @PathVariable int page
    ) {
        log.info("밑줄 긋기 생성 요청 확인 question id = {}", questionId);

        RsData<Question> questionRs = questionService.findById(questionId);

        if (questionRs.isFail()) {
            log.info("존재하지 않는 문제 msg = {}", questionRs.getMsg());
            return rq.historyBack(questionRs.getMsg());
        }

        RsData<Underline> underlineRs = underlineService.create(form, questionRs.getData(), rq.getMember());

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

    //-- 문제집 별 밑줄 문제 목록 --//
    @GetMapping("/book/{id}")
    public String book(
            @PathVariable Long id,
            QuestionSearchForm form,
            Model model
    ){
        log.info("문제집 별 밑줄 문제 목록 요청 확인 book id = {}", id);
        RsData<Book> bookRs = bookService.findById(id);

        if (bookRs.isFail()) {
            log.info("목록 조회 실패 msg = {}", bookRs.getMsg());
            return rq.historyBack(bookRs.getMsg());
        }

        List<Underline> underlines = bookRs.getData().getUnderlines();
        form.setSelect(1);

        boolean like = false;
        if (rq.isLogin())
            like = bookService.isLike(rq.getMember(), bookRs.getData());

        model.addAttribute("underlines", underlines);
        model.addAttribute("book", bookRs.getData());
        model.addAttribute("like", like);
        model.addAttribute("mcq", MCQ);

        log.info("문제집 별 밑줄 문제 목록 응답 완료 underlines size = {}", underlines.size());
        return "usr/underline/top/book";
    }


    //-- underline 문제 상세페이지 --//
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("밑줄 문제 상세페이지 요청 확인 underline id = {}", id);

        RsData<Underline> underlineRs = underlineService.findById(id);

        if (underlineRs.isFail()) {
            log.info("밑줄 목록 조회 실패 msg = {}", underlineRs.getMsg());
            return rq.historyBack(underlineRs.getMsg());
        }

        Underline underline = underlineRs.getData();
        Question question = underline.getQuestion();

        if (rq.isLogout() || rq.getMember() != underline.getBook().getMember())
            return "redirect:/question/detail/" + question.getId();

        AnswerCheck check = question.getAnswerCheck();


        model.addAttribute("underline", underline);
        model.addAttribute("question", question);
        model.addAttribute("check", check);
        model.addAttribute("mcq", MCQ);

        log.info("문제 상세페이지 응답 완료 underline id = {}", id);
        return "usr/underline/top/detail";
    }

    //-- 오답 노트 수정 --//
    @PostMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String update(
            @PathVariable Long id,
            String about
    ) {
        log.info("오답 노트 수정 요청 확인 id = {}", id);
        RsData<Underline> underlineRs = underlineService.findById(id);

        if (underlineRs.isFail()) {
            log.info("id 조회 실패 msg = {}", underlineRs.getMsg());
            return rq.historyBack(underlineRs.getMsg());
        }

        RsData<Underline> updateRs = underlineService.update(underlineRs.getData(), about);

        log.info("오답노트 수정 완료 id = {}", id);
        return rq.redirectWithMsg("/underline/detail/" + underlineRs.getData().getQuestion().getId(), updateRs.getMsg());
    }

    //-- 밑줄 삭제 --//
    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id) {
        log.info("밑줄 삭제 요청 확인 id = {}", id);

        RsData<Underline> underlineRs = underlineService.findById(id);
        if (underlineRs.isFail()) {
            log.info("id 조회 실패 msg = {}", underlineRs.getMsg());
            return rq.historyBack(underlineRs.getMsg());
        }
        Long categoryId = underlineRs.getData().getQuestion().getCategory().getId();
        underlineService.delete(underlineRs.getData());

        log.info("밑줄 삭제 완료");
        return rq.redirectWithMsg("/underline/list/" + categoryId, "삭제가 완료되었습니다.");
    }
}
