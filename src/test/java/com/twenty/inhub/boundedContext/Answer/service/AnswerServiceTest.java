package com.twenty.inhub.boundedContext.Answer.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerQueryRepository;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerRepository;
import com.twenty.inhub.boundedContext.Answer.service.AnswerService;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.controller.form.CreateSubjectiveForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.question.repository.QuestionRepository;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class AnswerServiceTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerQueryRepository answerQueryRepository;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionService questionService;

    @Autowired
    CategoryService categoryService;

    @Test
    @DisplayName("문제 생성시 답변 등록을 해야함.")
    public void testCreateAnswer() {
        Member member = member();
        CreateCategoryForm cateform = new CreateCategoryForm("category1","about1");
        CreateSubjectiveForm form = new CreateSubjectiveForm("주관식", "내용", "태그1, 태그2");
        RsData<Category> category = categoryService.create(cateform);
        RsData<Question> questionRs = questionService.create(form, member, category.getData());
        Question question = questionRs.getData();

        assertThat(questionRs.isSuccess()).isTrue();

        assertThat(question.getType()).isEqualTo(QuestionType.SUBJECTIVE);
        assertThat(question.getName()).isEqualTo("주관식");
        assertThat(question.getContent()).isEqualTo("내용");



        RsData<Answer> answer = answerService.createAnswer(question,member,"주관식","내용","입니다.");

        String content = "안녕하세요요요요용요요요요요요용 저는 사람 주관식 내용 입니다. 람쥐 썬더";
        RsData<Answer> contentAnswer = answerService.checkAnswer(question,content);

        assertThat(question.getAnswers().size()).isEqualTo(1);
        assertThat(contentAnswer.getMsg()).isEqualTo("3개 일치");

    }

    @Test
    @DisplayName("현재 질문의 답 삭제시 없어져야함.")
    public void testDeleteAnswer(){
        Member member = member();
        CreateCategoryForm cateform = new CreateCategoryForm("category1","about1");
        CreateSubjectiveForm form = new CreateSubjectiveForm("주관식", "내용", "태그1, 태그2");
        RsData<Category> category = categoryService.create(cateform);
        RsData<Question> questionRs = questionService.create(form, member, category.getData());
        Question question = questionRs.getData();

        RsData<Answer> answer = answerService.checkAnswer(question,"주관식 내용입니다.");

        answerService.deleteAnswer(answer.getData());
        assertThat(question.getAnswers().size()).isEqualTo(0);
    }

    //답 검색용
    @Test
    @DisplayName("답 찾기")
    public void testFindAnswer() {
        Member member = member();
        CreateCategoryForm cateform = new CreateCategoryForm("category1","about1");
        CreateSubjectiveForm form = new CreateSubjectiveForm("주관식", "내용", "태그1, 태그2");
        RsData<Category> category = categoryService.create(cateform);
        RsData<Question> questionRs = questionService.create(form, member, category.getData());

        Question question = questionRs.getData();

        RsData<Answer> answer = answerService.checkAnswer(question,"주관식 답입니다.");
        RsData<Answer> answer2 = answerService.checkAnswer(question,"주관식 2답입니다.");



    }

    @Test
    @DisplayName("답 수정")
    public void testUpdateAnswer(){
        Member member = member();
        CreateCategoryForm cateform = new CreateCategoryForm("category1","about1");
        CreateSubjectiveForm form = new CreateSubjectiveForm("주관식", "내용", "태그1, 태그2");
        RsData<Category> category = categoryService.create(cateform);
        RsData<Question> questionRs = questionService.create(form, member, category.getData());

        Question question = questionRs.getData();

        RsData<Answer> answer = answerService.checkAnswer(question,"주관식 답입니다.");

        Answer updateAnswer = answerService.updateAnswer(answer.getData(),"주관식 답2입니다.");

        assertThat(question.getAnswers().get(0).getContent()).isEqualTo("주관식 답2입니다.");
    }

    //임시 조치
    private Member member(){

        Member member = Member.builder()
                .role(MemberRole.SENIOR)
                .build();

        memberRepository.save(member);

        return member;
    }
}