package com.twenty.inhub.base.initData;

import com.twenty.inhub.boundedContext.answer.service.AnswerService;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.post.dto.PostDto;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.service.PostService;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import com.twenty.inhub.boundedContext.underline.UnderlineService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.twenty.inhub.boundedContext.post.entity.QPost.post;
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
            PostService postService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member memberAdmin = memberService.create("admin", "1234").getData();
                Member user1 = memberService.create("user1", "1234").getData();

                //-- 카테고리 init data 추가 --//
                Category network = createCategory("네트워크");
                Category os = createCategory("운영체제");
                createCategory("데이터베이스");
                createCategory("알고리즘");
                createCategory("암호학/보안");
                createCategory("컴파일러");

                //-- 네트워크, 운영체제에 객관식 문제 추가 --//
                for (int i = 0; i < 5; i++) {
                    createMCQ(network, i + "번 문제", "Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem quasi. In deleniti eaque aut repudiandae et a id nisi.");
                    createMCQ(os, i + "번 문제", "Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem quasi. In deleniti eaque aut repudiandae et a id nisi.");
                }

                //-- 네트워크, 운영체제 주관식 문제 추가 --//
                for (int i = 0; i < 5; i++) {
                    createSAQ(network, i + 3 + "번 문제", "Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem quasi. In deleniti eaque aut repudiandae et a id nisi.");
                    createSAQ(os, i + 3 + "번 문제", "Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem quasi. In deleniti eaque aut repudiandae et a id nisi.");
                }

                // 밑줄 친 문제 설정
                for (int i = 1; i <= 15; i++) {
                    underlineService.create("오답" + i, user1, questionService.findById((long) i).getData());
                }

                // 초기 게시글 생성
                Member admin = memberService.create("admin", "1234").getData();
                createPost(postService, "팀20", "멋사 팀 프로젝트 팀20 입니다.", admin);
                createPost(postService, "InHub", "면접 예상 질문들을 풀어보며 공부해볼 수 있는 사이트 입니다.", admin);
                for (int i = 1; i <= 100; i++) {
                    createPost(postService, "초기 게시글" + i, "내용" + i, admin);
                }
            }

            // 카테고리 생성 //
            private Category createCategory(String name) {
                return categoryService.create(new CreateCategoryForm(name, name + " 와 관련된 문제")).getData();
            }

            // 객관식 문제 생성 //
            private void createMCQ(Category category, String name, String content) {

                Member admin = memberService.findByUsername("admin").get();

                List<String> choice = new ArrayList<>();
                choice.add("1번 선택지");
                choice.add("2번 선택지");
                choice.add("3번 선택지");

                CreateQuestionForm form = new CreateQuestionForm(name, content, "태그1, 태그2, 태그3", choice, category.getId(), MCQ);
                Question question = questionService.create(form, admin, category).getData();

                answerService.createAnswer(question, admin, "0");
            }

            // 주관식 문제 생성 //
            private void createSAQ(Category category, String name, String content) {

                Member admin = memberService.findByUsername("admin").get();

                List<String> choice = new ArrayList<>();

                CreateQuestionForm form = new CreateQuestionForm(name, content, "태그1, 태그2, 태그3", choice, category.getId(), SAQ);
                Question question = questionService.create(form, admin, category).getData();

                answerService.createAnswer(question, admin, "키", "워", "드");
            }

            // 초기 게시글 생성 //
            private void createPost(PostService postService, String title, String content, Member member) {
                PostDto postDto = new PostDto();
                postDto.setTitle(title);
                postDto.setContent(content);
                postDto.setCreatedTime(LocalDateTime.now());
                postDto.setPostHits(0);
                postDto.setAuthor(member);
                postService.createPost(postDto);
            }
        };
    }
}
