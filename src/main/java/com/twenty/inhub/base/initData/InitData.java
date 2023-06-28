package com.twenty.inhub.base.initData;

import com.twenty.inhub.base.appConfig.CustomMultipartFile;
import com.twenty.inhub.boundedContext.answer.controller.AnswerController.AnswerCheckForm;
import com.twenty.inhub.boundedContext.answer.service.AnswerService;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.service.BookService;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.controller.form.MemberJoinForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.post.dto.PostDto;
import com.twenty.inhub.boundedContext.post.service.PostService;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import com.twenty.inhub.boundedContext.underline.UnderlineService;
import com.twenty.inhub.boundedContext.underline.dto.UnderlineCreateForm;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;
import static com.twenty.inhub.boundedContext.question.entity.QuestionType.SAQ;


@Profile("dev")
@Configuration
public class InitData {

    @Bean
    CommandLineRunner init(
            MemberService memberService,
            CategoryService categoryService,
            QuestionService questionService,
            UnderlineService underlineService,
            AnswerService answerService,
            PostService postService,
            BookService bookService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {

                //-- user 추가 --//
                Member memberAdmin = memberService.create(new MemberJoinForm("admin", "1234", "", "ADMIN")).getData();
                Member user1 = memberService.create(new MemberJoinForm("user1", "1234", "", "USER1")).getData();

                //-- 카테고리 init data 추가 --//
                Category network = createCategory("네트워크");
                Category os = createCategory("운영체제");
                createCategory("데이터베이스");
                createCategory("알고리즘");
                createCategory("암호학/보안");
                createCategory("컴파일러");


                //-- 네트워크, 운영체제 주관식 문제 추가 --//
                for (int i = 0; i < 5; i++) {
                    createSAQ(network, i + 3 + "번 문제", "Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem quasi. In deleniti eaque aut repudiandae et a id nisi.", i);
                    createSAQ(os, i + 3 + "번 문제", "Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem quasi. In deleniti eaque aut repudiandae et a id nisi.", i);
                }


//                //-- 초기 게시글 생성 --//
//                createPost(postService, "팀20", "멋사 팀 프로젝트 팀20 입니다.", memberAdmin);
//                createPost(postService, "InHub", "면접 예상 질문들을 풀어보며 공부해볼 수 있는 사이트 입니다.", memberAdmin);
//                for (int i = 1; i <= 100; i++) {
//                    createPost(postService, "초기 게시글" + i, "내용" + i, memberAdmin);
//                }

                //-- 네트워크, 운영체제에 객관식 문제 추가 --//
                for (int i = 0; i < 5; i++) {
                    createMCQ(network, i + "번 문제", "Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem quasi. In deleniti eaque aut repudiandae et a id nisi.", i);
                    createMCQ(os, i + "번 문제", "Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem quasi. In deleniti eaque aut repudiandae et a id nisi.", i);
                }

               

                //-- 더미 문제집 생성 --//
                for (int i = 1; i < 9; i++)
                    createBook(memberAdmin, "문제집" + i, "태그" + i + ", 태그" + (i + 1) + ", 태그" + (i + 2), "static/images/book/" + i + ".png");

                //-- 문제집에 밑줄 추가 --//
                createUnderline(bookService.findById(1L).getData(), memberAdmin);
                createUnderline(bookService.findById(8L).getData(), memberAdmin);
            }


            //------------ CREATE METHOD ---------------//

            // 밑줄 생성 //
            private void createUnderline(Book book, Member member) {
                for (int i = 1; i < 4; i++) {

                    Question question = questionService.findById((long) i).getData();

                    UnderlineCreateForm form = new UnderlineCreateForm();
                    form.setBookId(book.getId());
                    form.setAbout("cupiditate voluptatem et in. Quaerat  ut assumenda excepturi  quasi.");
                    underlineService.create(form, question, member);
                }
            }

            // Book 생성 //
            private Book createBook(Member member, String name, String tag, String img) throws IOException {
                Resource resource = new ClassPathResource(img);
                File file = resource.getFile();
                MultipartFile mFile = new CustomMultipartFile(file);
                BookCreateForm form = new BookCreateForm(name, "cupiditate voluptatem et in. Quaerat  ut assumenda excepturi  quasi.", tag, mFile);

                return bookService.create(form, member).getData();
            }

            // 카테고리 생성 //
            private Category createCategory(String name) {
                return categoryService.create(new CreateCategoryForm(name, name + " 와 관련된 문제")).getData();
            }

            // 객관식 문제 생성 //
            private void createMCQ(Category category, String name, String content, int i) {

                Member admin = memberService.findByUsername("admin").get();

                List<String> choice = new ArrayList<>();
                choice.add("1번 선택지");
                choice.add("2번 선택지");
                choice.add("3번 선택지");

                CreateQuestionForm form = new CreateQuestionForm(name, content, "태그" + i + ", 태그" + (i + 1) + ", 태그" + (i + 2), choice, category.getId(), MCQ);
                Question question = questionService.create(form, admin, category).getData();

                answerService.createAnswer(question, admin, "0");
            }

            // 주관식 문제 생성 //
            private void createSAQ(Category category, String name, String content, int i) {

                Member admin = memberService.findByUsername("admin").get();

                List<String> choice = new ArrayList<>();

                CreateQuestionForm form = new CreateQuestionForm(name, content, "태그" + i + ", 태그" + (i + 1) + ", 태그" + (i + 2), choice, category.getId(), SAQ);
                Question question = questionService.create(form, admin, category).getData();

                answerService.createAnswer(question, admin, new AnswerCheckForm("키,이,워,어,드,으"));
            }

//            // 초기 게시글 생성 //
//            private void createPost(PostService postService, String title, String content, Member member) {
//                PostDto postDto = new PostDto();
//                postDto.setTitle(title);
//                postDto.setContent(content);
//                postDto.setCreatedTime(LocalDateTime.now());
//                postDto.setPostHits(0);
//                postDto.setAuthor(member);
//                postService.createPost(postDto, member);
//            }
        };
    }
}
