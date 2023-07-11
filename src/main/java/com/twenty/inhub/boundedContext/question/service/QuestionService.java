package com.twenty.inhub.boundedContext.question.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.controller.AnswerController.AnswerCheckForm;
import com.twenty.inhub.boundedContext.answer.service.AnswerService;
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.QuestionReqDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.QuestionResDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateListReqDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateListResDto;
import com.twenty.inhub.boundedContext.question.controller.dto.QuestionResOpenDto;
import com.twenty.inhub.boundedContext.question.controller.form.CreateFunctionForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.controller.form.QuestionSearchForm;
import com.twenty.inhub.boundedContext.question.controller.form.UpdateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Choice;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.question.entity.Tag;
import com.twenty.inhub.boundedContext.question.event.dto.QuestionSolveDto;
import com.twenty.inhub.boundedContext.question.repository.QuestionQueryRepository;
import com.twenty.inhub.boundedContext.question.repository.QuestionRepository;
import com.twenty.inhub.boundedContext.question.repository.TagRepository;
import com.twenty.inhub.boundedContext.underline.Underline;
import com.twenty.inhub.boundedContext.underline.UnderlineService;
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
    private final AnswerService answerService;
    private final UnderlineService underlineService;
    private final TagRepository tagRepository;


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
        List<QuestionReqDto> reqDtoList = dto.getReqDtoList();

        List<QuestionResDto> resDtoList = new ArrayList<>();

        for (int i = 0; i < reqDtoList.size(); i++) {
            QuestionResDto resDto = new QuestionResDto();
            QuestionReqDto reqDto = reqDtoList.get(i);

            // Question 등록
            Question question = Question.createSAQ(reqDto, member, category);
            Question saveQuestion = questionRepository.save(question);
            resDto.setQuestionId(saveQuestion.getId());

            // Answer Check 등록
            String keyWord = reqDto.getKeyWord();
            Long answerId = answerService.createAnswer(saveQuestion, member, new AnswerCheckForm(keyWord)).getData().getId();
            resDto.setAnswerId(answerId);

            // res dto 인덱스 추가
            resDtoList.add(resDto);
        }

        UpdateListResDto responseDto = new UpdateListResDto();
        responseDto.setReqDtoList(resDtoList);
        responseDto.setCount(resDtoList.size());
        return responseDto;
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
                form.getCount(),
                form.getUnderlines()
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
     * name, content, choice, tag update
     * EVENT : update difficult
     */

    //-- name, content, choice, tag update --//
    @Transactional
    public RsData<Question> update(Question question, UpdateQuestionForm form) {

        List<Tag> tags = question.getTags();
        for (Tag tag : tags) tagRepository.delete(tag);

        Question saveQuestion = questionRepository.save(question.updateQuestion(form));
        return RsData.of("S-1", "수정이 완료되었습니다.", saveQuestion);
    }

    //-- EVENT : update difficult --//
    public void updateDifficulty(List<QuestionSolveDto> dtoList) {

        for (QuestionSolveDto dto : dtoList) {
            Question question = dto.getQuestion();
            int challenger = question.updateScore(question, dto.getScore());

            if (challenger > 0)
                question.updateDifficulty(question);

            questionRepository.save(question);
        }
    }


    /**
     * ** BUSINESS LOGIC **
     * find All Question Type
     * find difficulty list
     * find by name & tag
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

    //-- find by name & tag --//
    public PageResForm<Question> findByInput(SearchForm form) {
        return questionQueryRepository.findByInput(form);
    }

    //-- find by name & tag (open api) --//
    public PageResForm<QuestionResOpenDto> findDtoByInput(SearchForm form) {
        return questionQueryRepository.findDtoByInput(form);
    }


    /**
     * ** DELETE METHOD **
     * delete question
     */

    //-- delete question --//
    @Transactional
    public RsData delete(Question question) {

        List<Underline> underlines = question.getUnderlines();
        for (int i = 0; i < underlines.size(); i++)
            underlineService.delete(underlines.get(i));

        question.deleteQuestion();
        questionRepository.delete(question);
        return RsData.of("S-1", "삭제가 완료되었습니다.");
    }
}