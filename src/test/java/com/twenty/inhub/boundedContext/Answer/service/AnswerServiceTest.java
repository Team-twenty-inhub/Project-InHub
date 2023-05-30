package com.twenty.inhub.boundedContext.Answer.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerQueryRepository;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerRepository;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

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


    //임시 조치
    private Member member(){

        Member member = Member.builder()
                .role(MemberRole.SENIOR)
                .build();

        memberRepository.save(member);

        return member;
    }
}