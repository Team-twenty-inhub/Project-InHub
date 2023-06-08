package com.twenty.inhub.boundedContext.question.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.QuestionReqDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.QuestionResDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateListReqDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateListResDto;
import com.twenty.inhub.boundedContext.question.controller.form.CreateFunctionForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.controller.form.QuestionSearchForm;
import com.twenty.inhub.boundedContext.question.entity.Choice;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.question.entity.Tag;
import com.twenty.inhub.boundedContext.question.repository.QuestionQueryRepository;
import com.twenty.inhub.boundedContext.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;
import static com.twenty.inhub.boundedContext.question.entity.QuestionType.SAQ;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionQueryRepository questionQueryRepository;


    /**
     * ** Create Method **
     * create question
     * 주관식 대량등록
     */

    //-- create question --//
    @Transactional
    public RsData<Question> create(CreateQuestionForm form, Member member, Category category) {

        if (!member.getRole().equals(MemberRole.ADMIN))
            return RsData.of("F-1", "권한이 없습니다.");

        List<Tag> tags = createTags(form.getTags());
        Question question = buildQuestion(form, member, category, tags);
        Question saveQuestion = questionRepository.save(question);


        return RsData.of("S-1", "문제가 등록되었습니다.", saveQuestion);
    }

    // type 별 question 객체 생성 //
    private static Question buildQuestion(CreateQuestionForm form, Member member, Category category, List<Tag> tags) {
        if (form.getType() == MCQ) {
            List<Choice> choice = createChoice(form.getChoiceList());
            return Question.createQuestion(form, choice, tags, member, category);

        } else
            return Question.createQuestion(form, tags, member, category);
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
        for (int i = 0; i < choice.size(); i++)
            choiceList.add(Choice.createChoice(choice.get(i), i));

        return choiceList;
    }

    //-- 주관식 대량 등록 --//
    @Transactional
    public UpdateListResDto createQuestions(UpdateListReqDto dto, Member member, Category category) {
        List<QuestionReqDto> questionDtoList = dto.getQuestionReqDtoList();

        List<QuestionResDto> questionResDtoList = new ArrayList<>();

        for (int i = 0; i < questionDtoList.size(); i++) {
            QuestionReqDto questionDto = questionDtoList.get(i);
            Question createQuestion = Question.createSAQ(questionDto, member, category);
            Question question = questionRepository.save(createQuestion);

            QuestionResDto questionResDto = new QuestionResDto();
            questionResDto.setId(question.getId());
            questionResDtoList.add(questionResDto);

            // answerService
        }

        UpdateListResDto resDto = new UpdateListResDto();
        resDto.setQuestionResDtoList(questionResDtoList);
        resDto.setCount(questionResDtoList.size());
        return resDto;
    }


    /**
     * ** READ METHOD **
     * find by id
     * get playlist
     * find by id list
     * find by input
     * find all
     */

    //-- find by id --//
    public RsData<Question> findById(Long id) {
        Optional<Question> byId = questionRepository.findById(id);

        if (byId.isPresent())
            return RsData.of(byId.get());

        return RsData.of("F-1", id + " 는 존재하지 않는 id 입니다.");
    }

    //-- get playlist --//
    public List<Long> getPlaylist(CreateFunctionForm form) {
        return questionQueryRepository.playlist(
                form.getCategories(),
                form.getType(),
                form.getDifficulties(),
                form.getCount()
        );
    }

    //-- find by id list --//
    public List<Question> findByIdList(List<Long> id) {
        return questionQueryRepository.findById(id);
    }

    //-- find by input --//
    public List<Question> findByInput(QuestionSearchForm form) {
        List<Question> questions = questionQueryRepository.findByInput(form);

        if (questions.size() == 0) return null;

        return questions;
    }

    //-- find all --//
    public List<Question> findAll() {
        return questionRepository.findAll();
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


    /**
     * ** BUSINESS LOGIC **
     * find All Question Type
     * find difficulty list
     * type mapper
     */

    //-- find all question type --//
    public List<QuestionType> findQuestionType() {
        List<QuestionType> types = new ArrayList<>();
        types.add(SAQ);
        types.add(MCQ);
        return types;
    }

    //-- find difficulty list --//
    public List<Integer> findDifficultyList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) list.add(i);
        return list;
    }

    public QuestionType typeMapper(int type) {
        return switch (type) {
            case 1 -> MCQ;
            case 2 -> SAQ;
            default -> null;
        };
    }

    //-- find answer by member & question 임시 매서드 --//
    public RsData<Answer> findAnswerByQustionMember(Question question, Member member) {
        List<Answer> answers = questionQueryRepository.findAnswerByQustionMember(question, member);

        if (answers.size() == 0)
            return RsData.of("F-1", "등록된 정답 없음");

        return RsData.of(answers.get(0));
    }
}
