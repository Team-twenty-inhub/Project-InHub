package com.twenty.inhub.boundedContext.question.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.question.controller.form.CreateChoiceForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateSubjectiveForm;
import com.twenty.inhub.boundedContext.question.entity.Choice;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.Tag;
import com.twenty.inhub.boundedContext.question.repository.ChoiceRepository;
import com.twenty.inhub.boundedContext.question.repository.QuestionQueryRepository;
import com.twenty.inhub.boundedContext.question.repository.QuestionRepository;
import com.twenty.inhub.boundedContext.question.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final ChoiceRepository choiceRepository;
    private final QuestionQueryRepository questionQueryRepository;


    /**
     * ** Create Method **
     * create 주관식 - 삭제 예정
     * create 객관식 - 삭제 예정
     * create question
     */

    //-- create 주관식 question - 삭제 예정 --//
    @Transactional
    public RsData<Question> create(CreateSubjectiveForm form, Member member, Category category) {

        if (!member.getRole().equals(MemberRole.SENIOR))
            return RsData.of("F-1", "권한이 없습니다.");

        Question question = Question.createSubjective(form, member, category);
        Question saveQuestion = questionRepository.save(question);

        return RsData.of("S-1", "주관식 문제가 등록되었습니다.", saveQuestion);
    }

    //-- create 객관식 question - 삭제 예정 --//
    @Transactional
    public RsData<Question> create(CreateChoiceForm form, Member member, Category category) {

        if (!member.getRole().equals(MemberRole.SENIOR))
            return RsData.of("F-1", "권한이 없습니다.");

        Question question = Question.createChoice(form, member, category);
        Question saveQuestion = questionRepository.save(question);

        return RsData.of("S-1", "주관식 문제가 등록되었습니다.", saveQuestion);
    }

    //-- create question --//
    @Transactional
    public RsData<Question> create(CreateQuestionForm form, Member member, Category category) {

        if (!member.getRole().equals(MemberRole.ADMIN))
            return RsData.of("F-1", "권한이 없습니다.");

        List<Tag> tags = createTags(form.getTags());
        List<Choice> choice = createChoice(form.getChoiceList());

        Question question = Question.createQuestion(form, choice, tags, member, category);
        Question saveQuestion = questionRepository.save(question);

        return RsData.of("S-1", "문제가 등록되었습니다.", saveQuestion);
    }

    // create tag //
    private static List<Tag> createTags(List<String> tags) {
        List<Tag> tagList = new ArrayList<>();
        for (String tag : tags)
            tagList.add(Tag.createTag(tag));
        return tagList;
    }

    // create choice //
    private static List<Choice> createChoice(List<String> choice) {
        List<Choice> choiceList = new ArrayList<>();
        for (String choose : choice)
            choiceList.add(Choice.createChoice(choose));
        return choiceList;
    }


    /**
     * ** READ METHOD **
     * find by id
     */

    //-- find by id --//
    public RsData<Question> findById(Long id) {
        Optional<Question> byId = questionRepository.findById(id);

        if (byId.isPresent())
            return RsData.successOf(byId.get());

        return RsData.of("F-1", id + " 는 존재하지 않는 id 입니다.");
    }


    /**
     * ** UPDATE METHOD **
     * update name, content
     */

    //-- update name, content --//
    @Transactional
    public RsData<Question> update(Question question, String name, String content) {

        Question saveQuestion = questionRepository.save(question.updateQuestion(name, content));
        return RsData.of("S-1", "수정이 완료되었습니다.", saveQuestion);
    }
}
