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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public static class createAnswerForm {

        @NotBlank(message = "정답은 필수입니다.")
        private final String content;
    }

    //대략적인 모습만 -> Question에 연결될경우 없어질 예정
    @GetMapping("/check/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String CreateCheckAnswer(AnswerCheckForm answerCheckForm){
        log.info("문제의 정답을 넣어둘 폼 시작");

        if(rq.getMember().getRole() == MemberRole.JUNIOR){
            return rq.historyBack("접근 권한이 없습니다.");
        }

        return "usr/answer/check/top/create";

    }

    //문제 출제자가 정답을 넣어 둘때
    @PostMapping("/check/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String CreateCheckAnswer(AnswerCheckForm answerCheckForm, @PathVariable Long id) {

        log.info("문제 출제자가 사용하는 정답폼 응답 확인");

        Member member = rq.getMember();
        if(member.getRole() == MemberRole.JUNIOR)
        {
            return rq.historyBack("접근 권한 없음");
        }

        RsData<Question> question = this.questionService.findById(id);

        if (question.isFail()) {
            return rq.historyBack(question.getMsg());
        }

        RsData<Answer> answer = answerService.createAnswer(question.getData(), member, answerCheckForm.getWord1(), answerCheckForm.getWord2(), answerCheckForm.getWord3());
        return "usr/question/top/detail";
    }

    //서술형 적을 폼 -> 마찬가지로 Question 질문에 연결될때 없어질수 있음.
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String CreateAnswer(createAnswerForm answerForm){
        log.info("정답 작성폼 요청");

        if(rq.getMember().getRole() == MemberRole.JUNIOR){
            return rq.historyBack("접근 권한이 없습니다.");
        }
        return "usr/answer/top/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String CreateAnswer(createAnswerForm answerForm,@PathVariable Long id){
        log.info("문제 정답 생성 처리 확인");

        Member member = rq.getMember();

        RsData<Question> question = this.questionService.findById(id);

        if(question.isFail()){
            return rq.historyBack(question.getMsg());
        }

        RsData<Answer> answer = answerService.checkAnswer(question.getData(),member,answerForm.content);

        if(answer.isFail()){
            return rq.historyBack(answer.getMsg());
        }

        //해당문제로 redirect 예정
        return rq.redirectWithMsg("/",answer.getMsg());
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String ShowUpdateAnswer(Model model,@PathVariable Long id){
        Answer answer = answerService.findAnswer(id);
        RsData<Answer> canUpdateData = answerService.canUpdateAnswer(rq.getMember(),answer);

        if(canUpdateData.isFail()){
            rq.historyBack(canUpdateData);
        }

        model.addAttribute("answer",answer);


        return "usr/answer/top/update";
    }



    /**
     * modify Answer
     *
     */
    @PostMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updateAnswer(createAnswerForm answerForm,@PathVariable Long id){
        RsData<Answer> answer = answerService.updateAnswer(id,rq.getMember(),answerForm.getContent());

        if (answer.isFail()){
            return rq.historyBack(answer);
        }
        return rq.redirectWithMsg("/",answer.getMsg());
    }
    /**
     * delete Answer
     */
    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteAnswer(@PathVariable Long id){
        Answer answer = this.answerService.findAnswer(id);

        RsData<Answer> CanActDeleteData = answerService.CanDeleteAnswer(rq.getMember(),answer);


        if(CanActDeleteData.isFail()){
            return rq.redirectWithMsg("/",CanActDeleteData.getMsg());
        }
        answerService.deleteAnswer(answer);

        return rq.redirectWithMsg("/","삭제가 완료되었습니다.");
    }
}

