package com.twenty.inhub.boundedContext.answer.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.controller.dto.AnswerDto;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import com.twenty.inhub.boundedContext.answer.service.AnswerService;
import com.twenty.inhub.boundedContext.category.CategoryService;
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
import org.springframework.transaction.annotation.Transactional;
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

    private final CategoryService categoryService;

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
    public String CreateMcqAnswer(createAnswerForm createAnswerForm, @PathVariable Long id, Model model) {

        if (rq.getMember().getRole() == MemberRole.JUNIOR) {
            return rq.historyBack("접근 권한이 없습니다.");
        }
        RsData<Question> question = questionService.findById(id);

        model.addAttribute("question", question.getData());

        return "usr/answer/mcq/top/create";
    }

    @PostMapping("/mcq/create")
    @PreAuthorize("isAuthenticated()")
    public String CreateMcqAnswer(createAnswerForm createAnswerForm, @RequestParam Long id) {

        Member member = rq.getMember();

        if (rq.getMember().getRole() == MemberRole.JUNIOR) {
            return rq.historyBack("접근 권한이 없습니다.");
        }

        RsData<Question> question = this.questionService.findById(id);

        if (question.isFail()) {
            return rq.historyBack(question.getMsg());
        }
        RsData<AnswerCheck> answer = answerService.createAnswer(question.getData(), member, createAnswerForm.getContent());

        return rq.redirectWithMsg("/question/list/" + question.getData().getCategory().getId(), "객관식 정답 등록완료");
    }

    //대략적인 모습만 -> Question에 연결될경우 없어질 예정
    @GetMapping("/check/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String CreateCheckAnswer(AnswerCheckForm answerCheckForm) {
        log.info("문제의 정답을 넣어둘 폼 시작");

        if (rq.getMember().getRole() == MemberRole.JUNIOR) {
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
        if (member.getRole() == MemberRole.JUNIOR) {
            return rq.historyBack("접근 권한 없음");
        }

        RsData<Question> question = this.questionService.findById(id);

        if (question.isFail()) {
            return rq.historyBack(question.getMsg());
        }

        RsData<AnswerCheck> answer = answerService.createAnswer(question.getData(), member, answerCheckForm.getWord1(), answerCheckForm.getWord2(), answerCheckForm.getWord3());
        return rq.redirectWithMsg("/question/list/" + question.getData().getCategory().getId(), "서술형 정답 등록완료");

    }

    //서술형 적을 폼 -> 마찬가지로 Question 질문에 연결될때 없어질수 있음.
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String CreateAnswer(createAnswerForm answerForm) {
        log.info("정답 작성폼 요청");

        if (rq.getMember().getRole() == MemberRole.JUNIOR) {
            return rq.historyBack("접근 권한이 없습니다.");
        }
        return "usr/answer/top/create";
    }

    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String CreateAnswer(createAnswerForm answerForm, @PathVariable Long id) {
        log.info("문제 정답 생성 처리 확인");

        Member member = rq.getMember();

        RsData<Question> question = this.questionService.findById(id);

        if (question.isFail()) {
            return rq.historyBack(question.getMsg());
        }

        RsData<Answer> answer = answerService.checkAnswer(question.getData(), member, answerForm.content);

        if (answer.isFail()) {
            return rq.historyBack(answer.getMsg());
        }

        //해당문제로 redirect 예정
        return rq.redirectWithMsg("/", answer.getMsg());
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String ShowUpdateAnswer(Model model, @PathVariable Long id) {
        Answer answer = answerService.findAnswer(rq.getMember().getId(), id);
        RsData<Answer> canUpdateData = answerService.canUpdateAnswer(rq.getMember(), answer);

        if (canUpdateData.isFail()) {
            rq.historyBack(canUpdateData);
        }

        model.addAttribute("answer", answer);


        return "usr/answer/top/update";
    }


    /**
     * modify Answer
     */
    @PostMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updateAnswer(createAnswerForm answerForm, @PathVariable Long id) {
        RsData<Answer> answer = answerService.updateAnswer(id, rq.getMember(), answerForm.getContent());

        if (answer.isFail()) {
            return rq.historyBack(answer);
        }
        return rq.redirectWithMsg("/", answer.getMsg());
    }

    /**
     * delete Answer
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteAnswer(@PathVariable Long id) {
        Answer answer = this.answerService.findAnswer(rq.getMember().getId(), id);

        RsData<Answer> CanActDeleteData = answerService.CanDeleteAnswer(rq.getMember(), answer);


        if (CanActDeleteData.isFail()) {
            return rq.redirectWithMsg("/", CanActDeleteData.getMsg());
        }
        answerService.deleteAnswer(answer);

        return rq.redirectWithMsg("/", "삭제가 완료되었습니다.");
    }

    //세션에 임시 저장때 사용됨.
    @PostMapping("/quiz/create")
    @PreAuthorize("isAuthenticated()")
    public String CreateQuizAnswer(@RequestParam(defaultValue = "0") int page, @RequestParam Long id, createAnswerForm createAnswerForm) {
        RsData<Question> question = questionService.findById(id);
        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");
        if (answerList == null) {
            answerList = new ArrayList<>();
        }

        answerList.add(answerService.checkAnswer(question.getData(), rq.getMember(), createAnswerForm.getContent()).getData());
        rq.getSession().setAttribute("answerList", answerList);


        return "redirect:/question/play?page=%s".formatted(page);
    }

    //결과 저장하기 누를때 사용할 로직
    //아직 다 안만들어짐
    @PostMapping("/quiz/add")
    @PreAuthorize("isAuthenticated()")
    public String AddQuizAnswer() {

        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");
        for (Answer answer : answerList) {
            Question question = questionService.findById(answer.getQuestion().getId()).getData();
            answerService.AddAnswer(answer, rq.getMember(), question);
        }

        return rq.redirectWithMsg("/","결과 저장완료");
    }


    //퀴즈 정답 체크 결과 리스트
    //category가 지연로딩이라 가져올수없음.
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public String list(Model model) {
        log.info("퀴즈 결과 페이지 응답 요청");
        List<Long> playlist = (List<Long>) rq.getSession().getAttribute("playlist");
        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");


        List<Question> questions = questionService.findByIdList(playlist);

        List<AnswerDto> answerDtos = answerService.convertToDto(questions,answerList);
        model.addAttribute("answerDtos",answerDtos);

        log.info("퀴즈 전체 결과 페이지 응답 완료");

        return "usr/answer/top/list";

    }


    //상세페이지
    @GetMapping("/result/{id}")
    @PreAuthorize("isAuthenticated()")
    public String resultAnswer(@PathVariable int id, Model model) {

        List<Long> playlist = (List<Long>) rq.getSession().getAttribute("playlist");
        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");

        //문제번호 및 답 체크
        RsData<Question> question = questionService.findById(playlist.get(id - 1));
        Answer answer = answerList.get(id - 1);
        AnswerCheck answerCheck = answerService.findAnswerCheck(question.getData());


        model.addAttribute("question",question.getData());
        model.addAttribute("answer",answer);
        model.addAttribute("answerCheck",answerCheck);


        return "usr/answer/top/result";
    }

    @GetMapping("/result/comment/{id}")
    @PreAuthorize("isAuthenticated()")
    public String comment(@PathVariable Long id,Model model){
        RsData<Question> question = questionService.findById(id);
        List<Answer> answers = question.getData().getAnswers();

        model.addAttribute("answers",answers);

        return "usr/answer/top/comment";
    }


}

