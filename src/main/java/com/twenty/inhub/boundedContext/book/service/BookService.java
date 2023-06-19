package com.twenty.inhub.boundedContext.book.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.repository.BookRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.underline.Underline;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /**
     * ** CREATE METHOD **
     * create book
     */

    //-- create book --//
    @Transactional
    public RsData<Book> create(BookCreateForm form, Member member) {
        Book book = Book.createBook(form, member);
        Book saveBook = bookRepository.save(book);

        return RsData.of("S-1", "문제집이 생성되었습니다.", saveBook);
    }

    /**
     * ** SELECT METHOD **
     * find by id
     */

    //-- find by id --//
    public RsData<Book> findById(Long id) {
        Optional<Book> byId = bookRepository.findById(id);

        if (byId.isPresent())
            return RsData.of(byId.get());

        return RsData.of("F-1", "존재하지 않는 id");
    }
}
