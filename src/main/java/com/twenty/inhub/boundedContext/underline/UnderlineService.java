package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.service.BookService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.underline.dto.UnderlineCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnderlineService {

    private final UnderlineQueryRepository underlineQueryRepository;
    private final UnderlineRepository underlineRepository;
    private final BookService bookService;


    /**
     * ** CREATE METHOD **
     * create
     */

    //-- create --//
    @Transactional
    public RsData<Underline> create(UnderlineCreateForm form, Question question, Member member) {
        RsData<Book> bookRs = bookService.findById(form.getBookId());

        if (bookRs.isFail())
            bookRs = bookService.create(
                    new BookCreateForm("기본 문제집", "밑줄 문제 모음집"),
                    member
            );

        Book book = bookRs.getData();
        List<Underline> underlines = this.underlineQueryRepository.findByBookQuestion(book, question);

        if (underlines.size() > 0)
            return RsData.of("F-2", "이미 문제집에 포함된 문제입니다.");

        Underline underline = underlineRepository.save(
                Underline.createUnderline(form, book, question)
        );

        return RsData.of("S-1", question.getName() + " 문제 밑줄 긋기 완료", underline);
    }


    /**
     * ** READ METHOD **
     * find by about
     * find by id
     * find by book , question
     */

    //-- find by about --//
    public RsData<Underline> findByAbout(String about) {
        Optional<Underline> byAbout = underlineRepository.findByAbout(about);

        if (byAbout.isPresent())
            return RsData.of(byAbout.get());

        return RsData.of("F-1", "존재하지 않는 내용입니다.");
    }

    //-- find by id --//
    public RsData<Underline> findById(Long id) {
        Optional<Underline> byId = underlineRepository.findById(id);

        if (byId.isPresent())
            return RsData.of(byId.get());

        return RsData.of("F-1", "존재하지 않는 id 입니다.");
    }

    //-- find by book , question --//
    public List<Underline> findByBookQuestion(Book book, Question question) {
        return underlineQueryRepository.findByBookQuestion(book, question);
    }


    /**
     * ** UPDATE METHOD **
     * update about
     */

    //-- update about --//
    @Transactional
    public RsData<Underline> update(Underline underline, String about) {
        underline.updateAbout(about);
        return RsData.of("S-1", "오답노트 수정 완료", underline);
    }


    /**
     * ** DELETE METHOD **
     * delete
     */

    //-- delete --//
    @Transactional
    public void delete(Underline underline) {
        underline.delete();
        underlineRepository.delete(underline);
    }

    public List<Underline> listing(List<Underline> underlines, int category, int sortCode) {
        if(category != 0) {
            underlines = filteringBy(category, underlines);
        }

        return underlines.stream()
                .sorted(compareTo(sortCode))
                .collect(Collectors.toList());
    }

    private List<Underline> filteringBy(int category, List<Underline> underlines) {
        return underlines.stream()
                .filter(e -> e.getQuestion().getCategory().getId() == category)
                .collect(Collectors.toList());
    }

    private Comparator<Underline> compareTo(int sortCode) {
        return switch (sortCode) {
            case 1 -> Comparator.comparing(Underline::getId).reversed(); // 최신 순
            case 2 -> Comparator.comparing(Underline::getId); // 오래된 순
            default -> null;
        };
    }
}
