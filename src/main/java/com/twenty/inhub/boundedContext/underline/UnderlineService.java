package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.repository.BookRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.underline.dto.UnderlineCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnderlineService {

    private final UnderlineQueryRepository underlineQueryRepository;
    private final UnderlineRepository underlineRepository;
    private final BookRepository bookRepository;


    /**
     * ** CREATE METHOD **
     * create
     * book 에서 underline capy
     */

    //-- create --//
    @Transactional
    public RsData<Underline> create(UnderlineCreateForm form, Question question, Member member) {
        Optional<Book> bookById = bookRepository.findById(form.getBookId());

        if (!bookById.isPresent())
            bookById = Optional.of(bookRepository.save(Book.createBook(member)));

        Book book = bookById.get();
        List<Underline> underlines = this.underlineQueryRepository.findByBookQuestion(book, question);

        if (underlines.size() > 0)
            return RsData.of("F-2", "이미 문제집에 포함된 문제입니다.");

        Underline underline = underlineRepository.save(
                Underline.createUnderline(form, book, question)
        );

        return RsData.of("S-1", question.getName() + " 문제 밑줄 긋기 완료", underline);
    }

    //-- book 에서 underline capy --//
    public RsData copyFromBook(Book book, Underline underline) {

        if (book.getUnderlines().contains(underline))
            return RsData.of("F-1", underline.getQuestion().getName() + "문제는 이미 문제집에 수록되어 있습니다.");

        Underline save = underlineRepository.save(Underline.copyFromBook(book, underline));
        return RsData.of(save);
    }


    /**
     * ** READ METHOD **
     * find by id
     * find by book , question
     */

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

    //-- find by member , question --//
    public RsData<Underline> findByQuestionMember(Question question, Member member) {
        List<Underline> list = underlineQueryRepository.findByQuestionMember(question, member);

        if (list.size() == 0)
            return RsData.of("F-1", "밑줄이 존재하지 않습니다.");

        return RsData.of(list.get(0));
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
}
