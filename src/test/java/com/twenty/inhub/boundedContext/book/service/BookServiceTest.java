package com.twenty.inhub.boundedContext.book.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class BookServiceTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("Book 생성")
    void no1() {
        Member member = member();
        BookCreateForm form = new BookCreateForm("book", "about", "tag1, tag2");
        RsData<Book> bookRs = bookService.create(form, member);

        assertThat(bookRs.getResultCode()).isEqualTo("S-1");

        Book book = bookService.findById(bookRs.getData().getId()).getData();

        assertThat(book.getName()).isEqualTo("book");
        assertThat(book.getAbout()).isEqualTo("about");
        assertThat(book.getPlayCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("난수 생성 test")
    void no2() {
        for (int i = 0; i < 100; i++) {
            int random = bookService.random(10);
            assertThat(random < 10).isTrue();
            assertThat(random >= 0).isTrue();
        }
    }

    @Test
    @DisplayName("태그, name 한번에 검색")
    void no3() {
        Member member = member();
        for (int i = 0; i < 20; i++)
            book("book" + i, "태그" +i+ ",태그" +(i+1)+ ",태그" +(i+2), member);

        List<Book> all = bookService.findAll();
        assertThat(all.size()).isEqualTo(20);

        SearchForm form = new SearchForm();
        form.setInput("태그");
        form.setPage(0);
        PageResForm<Book> find1 = bookService.findByNameTag(form);

        assertThat(find1.getCount()).isEqualTo(20);
        assertThat(find1.getContents().size()).isEqualTo(16);

        form.setPage(1);
        PageResForm<Book> find2 = bookService.findByNameTag(form);
        assertThat(find2.getContents().size()).isEqualTo(4);

        form.setInput("7");
        form.setPage(0);
        PageResForm<Book> find3 = bookService.findByNameTag(form);
        assertThat(find3.getCount()).isEqualTo(6);

        form.setInput("book");
        form.setPage(0);
        PageResForm<Book> find4 = bookService.findByNameTag(form);
        assertThat(find4.getCount()).isEqualTo(20);
    }

    private Member member() {
        return memberService.create("admin", "1234").getData();
    }
    
    private Book book(String name, String tag, Member member) {
        BookCreateForm form = new BookCreateForm(name, "about", tag);
        return bookService.create(form, member).getData();
    }
}