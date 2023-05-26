package com.twenty.inhub.boundedContext.Answer.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import com.twenty.inhub.boundedContext.Answer.service.AnswerService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    private final QuestionService questionService;

    private final Rq rq;

    /**
     * AnswerForm
     * keyword
     */
    //출제자가 적는 정답
    @AllArgsConstructor
    @Getter
    public static class AnswerCheckForm {

        @NotBlank
        String word1;

        String word2;

        String word3;
    }

    /**
     * AnswerForm
     * content
     */
    //정답 적기용 Form
    @AllArgsConstructor
    @Getter
    public static class AnswerForm {

        @NotBlank
        private final String content;
    }

    //문제 출제자가 정답을 넣어 둘때
    @PostMapping("/check/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String CreateCheckAnswer(AnswerCheckForm answerCheckForm, @PathVariable Long id) {

        log.info("문제 출제자가 사용하는 정답폼 응답 확인");

        Member member = rq.getMember();

        RsData<Question> question = this.questionService.findById(id);

        if (question.isFail()) {
            return rq.historyBack(question.getMsg());
        }

        RsData<Answer> answer = answerService.createAnswer(question.getData(), member, answerCheckForm.getWord1(), answerCheckForm.getWord2(), answerCheckForm.getWord3());
        return rq.redirectWithMsg("/", answer.getMsg());
    }


    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String CreateAnswer(AnswerForm answerForm,@PathVariable Long id){

        log.info("문제 정답 생성 처리 확인");
        Member member = rq.getMember();

        if(member.getRole() == MemberRole.JUNIOR)
        {
            return rq.historyBack("접근 권한 없음");
        }

        RsData<Question> question = this.questionService.findById(id);

        if(question.isFail()){
            return rq.historyBack(question.getMsg());
        }

        RsData<Answer> answer = answerService.checkAnswer(question.getData(),member,answerForm.content);

        if(answer.isFail()){
            return rq.historyBack(answer.getMsg());
        }

        return rq.redirectWithMsg("/",answer.getMsg());
    }
    /**
     * modify Answer
     *
     */
    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modifyAnswer(AnswerForm answerForm,Member member,@PathVariable Long id){
        RsData<Answer> answer = answerService.updateAnswer(id,rq.getMember(),answerForm.getContent());

        return rq.redirectWithMsg("/",answer.getMsg());
    }
}

