package com.twenty.inhub.boundedContext.book.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
<<<<<<< HEAD
import com.twenty.inhub.boundedContext.book.controller.form.BookUpdateForm;
=======
>>>>>>> main
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
<<<<<<< HEAD
import com.twenty.inhub.boundedContext.question.entity.Tag;
=======
>>>>>>> main
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import com.twenty.inhub.boundedContext.underline.Underline;
import com.twenty.inhub.boundedContext.underline.UnderlineService;
import com.twenty.inhub.boundedContext.underline.dto.UnderlineCreateForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> main
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class BookServiceTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UnderlineService underlineService;

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
        PageResForm<Book> find1 = bookService.findByInput(form);

        assertThat(find1.getCount()).isEqualTo(20);
        assertThat(find1.getContents().size()).isEqualTo(16);

        form.setPage(1);
        PageResForm<Book> find2 = bookService.findByInput(form);
        assertThat(find2.getContents().size()).isEqualTo(4);

        form.setInput("7");
        form.setPage(0);
        PageResForm<Book> find3 = bookService.findByInput(form);
        assertThat(find3.getCount()).isEqualTo(6);

        form.setInput("book");
        form.setPage(0);
        PageResForm<Book> find4 = bookService.findByInput(form);
        assertThat(find4.getCount()).isEqualTo(20);
    }

    @Test
<<<<<<< HEAD
    @DisplayName("playlist 생성")
=======
    @DisplayName("playlsit 생성")
>>>>>>> main
    void no4() {
        Member member = member();
        Book book = book("book", "", member);
        Category category = category();

        for (int i = 0; i < 10; i++)
            underline("question" + i, member, category, book.getId());

        RsData<Book> bookRs = bookService.findById(book.getId());
        Book findBook = bookRs.getData();

        assertThat(bookRs.isSuccess()).isTrue();
        assertThat(findBook.getName()).isEqualTo("book");
        assertThat(findBook.getUnderlines().size()).isEqualTo(10);

        List<Long> playList = bookService.getPlayList(book).getData();

        assertThat(playList.size()).isEqualTo(10);

        long sum = playList.get(0) + playList.get(1) + playList.get(2);
        assertThat(sum != 3).isTrue();

        for (Long aLong : playList) System.out.println(aLong);
    }

<<<<<<< HEAD
    @Test
    @DisplayName("문제집 태그 수정")
    void no5() {
        Member member = member();
        Book book = book("book", "tag1, tag2, tag3", member);

        Book findBook = bookService.findById(book.getId()).getData();
        List<Tag> tags = findBook.getTagList();
        assertThat(tags.size()).isEqualTo(3);
        assertThat(tags.get(0).getTag()).isEqualTo("tag1");

        String tagString = "";
        for (Tag tag : tags) tagString += tag.getTag();
        assertThat(tagString).isEqualTo("tag1tag2tag3");

        BookUpdateForm form = new BookUpdateForm();
        form.setName("book"); form.setAbout("book");
        form.setTag("태그4, 태그5");
        form.setTerm(0L);
        form.setUnderlines(new ArrayList<>());

        RsData<Book> updateRs = bookService.update(book, form);
        List<Tag> tagList = updateRs.getData().getTagList();

        assertThat(updateRs.isSuccess()).isTrue();
        assertThat(tagList.size()).isEqualTo(2);
        assertThat(tagList.get(0).getTag()).isEqualTo("태그4");

        tagString = "";
        for (Tag tag : tagList) tagString += tag.getTag();
        assertThat(tagString).isEqualTo("태그4태그5");
    }







=======
>>>>>>> main
    private Member member() {
        return memberService.create("admin", "1234").getData();
    }
    
    private Book book(String name, String tag, Member member) {
        BookCreateForm form = new BookCreateForm(name, "about", tag);
        return bookService.create(form, member).getData();
    }

    private Category category() {
        CreateCategoryForm form = new CreateCategoryForm("category1", "about1");
        return categoryService.create(form).getData();
    }

    private void underline(String name, Member member, Category category, Long bookId) {

        Question question = questionService.create(
                new CreateQuestionForm(name, name, "", null, category.getId(), QuestionType.SAQ),
                member, category
        ).getData();

        UnderlineCreateForm form1 = new UnderlineCreateForm();
        form1.setBookId(bookId);
        form1.setAbout(name);
        underlineService.create(form1, question, member);
    }
}