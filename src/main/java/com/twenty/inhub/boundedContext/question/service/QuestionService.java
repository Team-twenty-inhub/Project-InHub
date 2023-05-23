package com.twenty.inhub.boundedContext.question.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.question.controller.form.CreateChoiceForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateSubjectiveForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.repository.QuestionQueryRepository;
import com.twenty.inhub.boundedContext.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionQueryRepository questionQueryRepository;


    /**
     * ** Create Method **
     * create 주관식
     * create 객관식
     */

    //-- create 주관식 question --//
    @Transactional
    public RsData<Question> create(CreateSubjectiveForm form, Member member, Category category) {

        if (!member.getRole().equals(MemberRole.SENIOR))
            return RsData.of("F-1", "권한이 없습니다.");

        Question question = Question.createSubjective(form, member, category);
        Question saveQuestion = questionRepository.save(question);

        return RsData.of("S-1", "주관식 문제가 등록되었습니다.", saveQuestion);
    }

    //-- create 주관식 question --//
    @Transactional
    public RsData<Question> create(CreateChoiceForm form, Member member, Category category) {

        if (!member.getRole().equals(MemberRole.SENIOR))
            return RsData.of("F-1", "권한이 없습니다.");

        Question question = Question.createChoice(form, member, category);
        Question saveQuestion = questionRepository.save(question);

        return RsData.of("S-1", "주관식 문제가 등록되었습니다.", saveQuestion);
    }

}
