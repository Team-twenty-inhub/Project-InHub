package com.twenty.inhub.boundedContext.book.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.twenty.inhub.base.appConfig.AwsS3MockConfig;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    private Member member() {
        return memberService.create("admin", "1234").getData();
    }
}