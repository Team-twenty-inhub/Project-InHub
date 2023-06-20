package com.twenty.inhub.boundedContext.answer.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.controller.AnswerController;
import com.twenty.inhub.boundedContext.answer.controller.AnswerController.AnswerCheckForm;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import com.twenty.inhub.boundedContext.answer.repository.AnswerCheckRepository;
import com.twenty.inhub.boundedContext.answer.repository.AnswerQueryRepository;
import com.twenty.inhub.boundedContext.answer.repository.AnswerRepository;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.controller.form.CreateQuestionForm;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import com.twenty.inhub.boundedContext.question.repository.QuestionRepository;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.twenty.inhub.boundedContext.question.entity.QuestionType.SAQ;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AnswerServiceTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerCheckRepository answerCheckRepository;

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
    @DisplayName("checkAnswer 검증")
    void checkAnswer(){
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

        AnswerCheckForm forms = new AnswerCheckForm("키,워,드");
        RsData<AnswerCheck> answerCheck = answerService.createAnswer(question,member,forms);


        RsData<Answer> answerRsData = answerService.checkAnswer(question,member,"키 워 드");

        assertThat(answerRsData.isSuccess()).isTrue();
        assertThat(answerRsData.getMsg()).isEqualTo("정답");

        RsData<Answer> answerRsData2 = answerService.checkAnswer(question,member,"키 드");
        assertThat(answerRsData2.isFail()).isTrue();
        assertThat(answerRsData2.getMsg()).isEqualTo("66점");


    }

    //임시 조치
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
        list.add(s1); list.add(s2); list.add(s3);
        return list;
    }

    private void question(String name, Category category, QuestionType type, Member member) {
        List<String> list = new ArrayList<>();
        CreateQuestionForm form = new CreateQuestionForm(name, "content", "태그1, 태그2, 태그3", list, category.getId(), type);
        questionService.create(form, member, category);
    }

    private Member member() {
        return memberService.create("admin", "1234").getData();
    }

    private Category category(String name) {
        return categoryService.create(new CreateCategoryForm(name, "about1")).getData();
    }
}