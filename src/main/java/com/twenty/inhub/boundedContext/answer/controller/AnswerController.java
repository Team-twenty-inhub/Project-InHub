package com.twenty.inhub.boundedContext.answer.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import com.twenty.inhub.boundedContext.answer.service.AnswerService;
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

import java.util.ArrayList;
import java.util.List;

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


    @GetMapping("/mcq/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String CreateMcqAnswer(createAnswerForm createAnswerForm,@PathVariable Long id,Model model){

        if (rq.getMember().getRole() == MemberRole.JUNIOR){
            return rq.historyBack("접근 권한이 없습니다.");
        }
        RsData<Question> question = questionService.findById(id);

        model.addAttribute("question",question.getData());

        return "usr/answer/mcq/top/create";
    }

    @PostMapping("/mcq/create")
    @PreAuthorize("isAuthenticated()")
    public String CreateMcqAnswer(createAnswerForm createAnswerForm,@RequestParam Long id){

        Member member = rq.getMember();

        if (rq.getMember().getRole() == MemberRole.JUNIOR){
            return rq.historyBack("접근 권한이 없습니다.");
        }

        RsData<Question> question = this.questionService.findById(id);

        if (question.isFail()) {
            return rq.historyBack(question.getMsg());
        }
        RsData<AnswerCheck> answer = answerService.createAnswer(question.getData(), member, createAnswerForm.getContent());

        return rq.redirectWithMsg("/question/list/" + question.getData().getCategory().getId(),"객관식 정답 등록완료");
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

    //문제 출제자가 정답을 넣어 둘때 -> 서술형
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

        RsData<AnswerCheck> answer = answerService.createAnswer(question.getData(), member, answerCheckForm.getWord1(), answerCheckForm.getWord2(), answerCheckForm.getWord3());
        return rq.redirectWithMsg("/question/list/" + question.getData().getCategory().getId(),"서술형 정답 등록완료");
        
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

    @PostMapping("/create/{id}")
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
        Answer answer = answerService.findAnswer(rq.getMember().getId(),id);
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
        Answer answer = this.answerService.findAnswer(rq.getMember().getId(), id);

        RsData<Answer> CanActDeleteData = answerService.CanDeleteAnswer(rq.getMember(),answer);


        if(CanActDeleteData.isFail()){
            return rq.redirectWithMsg("/",CanActDeleteData.getMsg());
        }
        answerService.deleteAnswer(answer);

        return rq.redirectWithMsg("/","삭제가 완료되었습니다.");
    }

    @PostMapping("/quiz/create")
    @PreAuthorize("isAuthenticated()")
    public String CreateQuizAnswer(@RequestParam(defaultValue = "0") int page,@RequestParam Long id,createAnswerForm createAnswerForm){
        RsData<Question> question = questionService.findById(id);
       RsData<Answer> answer = answerService.checkAnswer(question.getData(),rq.getMember(),createAnswerForm.getContent());


       return "redirect:/question/play?page=%s".formatted(page);
    }

    @GetMapping("/result")
    @PreAuthorize("isAuthenticated()")
    public String resultAnswer(Model model){
        log.info("퀴즈 전체 결과 페이지 응답 요청 ");

        List<Long> playlist = (List<Long>) rq.getSession().getAttribute("playlist");
        Member member = rq.getMember();

        //현재 생각
        List<Answer> answerList = new ArrayList<>();

        List<Question> questions = questionService.findByIdList(playlist);
        for(Question question : questions){
            answerList.add(answerService.findAnswer(member.getId(),question.getId()));
        }
        model.addAttribute("questions",questions);
        model.addAttribute("answerList",answerList);

        
        log.info("퀴즈 전체 결과 페이지 응답 완료");

        return "usr/answer/top/result";
    }

}

