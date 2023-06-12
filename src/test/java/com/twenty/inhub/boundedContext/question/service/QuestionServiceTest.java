package com.twenty.inhub.boundedContext.question.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.service.AnswerService;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.QuestionReqDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateListReqDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateListResDto;
import com.twenty.inhub.boundedContext.question.controller.form.CreateAnswerForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateFunctionForm;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.controller.form.QuestionSearchForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.underline.UnderlineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.MCQ;
import static com.twenty.inhub.boundedContext.question.entity.QuestionType.SAQ;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuestionServiceTest {

    @Autowired
    QuestionService questionService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    MemberService memberService;
    @Autowired
    UnderlineService underlineService;
    @Autowired
    AnswerService answerService;


    @Test
    void 문제_생성() {
        Member member = member();
        Category category = category("category");
        List<String> choice = createList("선택지1", "선택지2", "선택지3");
        CreateQuestionForm form = new CreateQuestionForm("주관식", "설명", "태그1, 태그2, 태그3", choice, category.getId(), SAQ);

        RsData<Question> questionRs = questionService.create(form, member, category);
        Question question = questionRs.getData();

        assertThat(questionRs.isSuccess()).isTrue();
        assertThat(question.getType()).isEqualTo(SAQ);
        assertThat(question.getName()).isEqualTo("주관식");
        assertThat(question.getContent()).isEqualTo("설명");
        assertThat(question.getTags().size()).isEqualTo(3);
    }

    @Test
    void 랜덤_문제_조회하기() {

        // test 용 member,category, question 생성 //
        Member member = member();
        List<Long> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Category category = category("cate" + i);
            categories.add(category.getId());
            for (int j = 0; j < 3; j++) {
                question("주관식" + j, category, SAQ, member, "태그1, 태그2, 태그3");
                question("객관식" + j, category, MCQ, member, "태그1, 태그2, 태그3");
            }
        }

        // 조건에 맞는 랜덤 문제 생성 //
        List<QuestionType> types = createType(SAQ);
        List<Integer> difficulties = createDif(0);
        categories.remove(3);
        categories.remove(3);
        CreateFunctionForm form = new CreateFunctionForm(categories, types, difficulties, null, 8);

        // 지연로딩으로 인한 세션에 문제 저장 안되는 문제를 해결하기 위해 id 필드만 조회 //
        List<Long> idList = questionService.getPlaylist(form);

        // id 가 일치하는 question 만 조회하는 method //
        // 어디서든 최근에 풀었던 playlist 를 조회할 수 있다. //
        List<Question> questions = questionService.findByIdList(idList);

        assertThat(questions.size()).isEqualTo(8);
        for (Question question : questions) {
            assertThat(question.getCategory().getName().equals("cate0") ||
                    question.getCategory().getName().equals("cate1") ||
                    question.getCategory().getName().equals("cate2")).isTrue();

            assertThat(question.getCategory().getName().equals("cate3")).isFalse();
            assertThat(question.getCategory().getName().equals("cate4")).isFalse();
            assertThat(question.getDifficulty()).isEqualTo(0);
            assertThat(question.getType()).isEqualTo(SAQ);
            assertThat(question.getType().equals(MCQ)).isFalse();
        }
    }

    @Test
    void 태그로_Question_조회() {
        // test 용 member,category, question 생성 //
        Member member = member();
        Category category = category("category");

        for (int i = 0; i < 10; i++)
            question("주관식" + i, category, SAQ, member, "태그" +i+ ",태그" +(i+1)+ ",태그" +(i+2));

        List<Question> all = questionService.findAll();
        assertThat(all.size()).isEqualTo(10);

        List<Question> by5 = questionService.findByInput(new QuestionSearchForm("5"));
        List<Question> by11 = questionService.findByInput(new QuestionSearchForm("11"));
        List<Question> by10 = questionService.findByInput(new QuestionSearchForm("10"));
        List<Question> by0 = questionService.findByInput(new QuestionSearchForm("0"));
        List<Question> by태그 = questionService.findByInput(new QuestionSearchForm("태그"));

        assertThat(by5.size()).isEqualTo(3);
        assertThat(by11.size()).isEqualTo(1);
        assertThat(by10.size()).isEqualTo(2);
        assertThat(by0.size()).isEqualTo(3);
        assertThat(by태그.size()).isEqualTo(10);
    }


    @Test
    void Question_대량_등록() {
        Member member = member();
        Category category = category("cate");

        UpdateListReqDto updateReqDto = new UpdateListReqDto();
        List<QuestionReqDto> dtoList = new ArrayList<>();
        updateReqDto.setCategoryId(category.getId());

        for (int i = 0; i < 10; i++) {
            QuestionReqDto reqDto = new QuestionReqDto();
            reqDto.setQuestion("질문" + i);
            reqDto.setKeyWord1("키" + i);
            reqDto.setKeyWord1("워" + i);
            reqDto.setKeyWord1("드" + i);
            dtoList.add(reqDto);
        }

        updateReqDto.setReqDtoList(dtoList);

        UpdateListResDto resDto = questionService.createQuestions(updateReqDto, member, category);

        List<Question> all = questionService.findAll();
        assertThat(all.size()).isEqualTo(10);
        assertThat(resDto.getCount()).isEqualTo(all.size());

        for (Question question : all) {
            assertThat(question.getType()).isEqualTo(SAQ);
            assertThat(question.getCategory()).isSameAs(category);
            assertThat(question.getMember().getUsername()).isEqualTo("admin");

            String name = question.getName().substring(0, 4);
            String content = question.getContent().substring(0, 2);
            assertThat(name).isEqualTo("cate");
            assertThat(content).isEqualTo("질문");
        }
    }

    @Test
    void Question_삭제() {
        Member member = member();
        Category category = category("cate");
        Question question1 = question("주관식", category, SAQ, member, "태그");
        Question question2 = question("객관식", category, MCQ, member, "태그");
        Question question3 = question("정답", category, MCQ, member, "태그");

        Question findQuestion = questionService.findById(question1.getId()).getData();
        List<Question> all3 = questionService.findAll();

        assertThat(findQuestion.getCategory()).isSameAs(category);
        assertThat(findQuestion.getName()).isEqualTo("주관식");
        assertThat(all3.size()).isEqualTo(3);

        RsData rsData = questionService.delete(findQuestion);
        assertThat(rsData.getResultCode()).isEqualTo("S-1");

        String resultCode1 = questionService.findById(question1.getId()).getResultCode();
        List<Question> all2 = questionService.findAll();

        assertThat(resultCode1).isEqualTo("F-1");
        assertThat(all2.size()).isEqualTo(2);

        underlineService.create("밑줄", member, question2);
        questionService.delete(question2);

        String resultCode2 = questionService.findById(question2.getId()).getResultCode();
        List<Question> all1 = questionService.findAll();

        assertThat(resultCode2).isEqualTo("F-1");
        assertThat(all1.size()).isEqualTo(1);

        answerService.createAnswer(question3, member, "1");
        assertThat(question3.getAnswerCheck().getContent()).isEqualTo("1");

        questionService.delete(question3);
        String resultCode3 = questionService.findById(question2.getId()).getResultCode();
        List<Question> all0 = questionService.findAll();

        assertThat(resultCode3).isEqualTo("F-1");
        assertThat(all0.size()).isEqualTo(0);
    }

    private List<QuestionType> createType(QuestionType type) {
        List<QuestionType> list = new ArrayList<>();
        list.add(type);
        return list;
    }

    private List<Integer> createDif(Integer i) {
        List<Integer> list = new ArrayList<>();
        list.add(i);
        return list;
    }

    private List<String> createList(String s1, String s2, String s3) {
        List<String> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        return list;
    }

    private Question question(String name, Category category, QuestionType type, Member member, String tag) {
        List<String> list = new ArrayList<>();
        CreateQuestionForm form = new CreateQuestionForm(name, "content", tag, list, category.getId(), type);
        return questionService.create(form, member, category).getData();
    }

    private Member member() {
        return memberService.create("admin", "1234").getData();
    }

    private Category category(String name) {
        return categoryService.create(new CreateCategoryForm(name, "about1")).getData();
    }

}