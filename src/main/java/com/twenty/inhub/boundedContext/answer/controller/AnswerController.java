package com.twenty.inhub.boundedContext.answer.controller;

import com.twenty.inhub.base.appConfig.AppConfig;
import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.controller.dto.AnswerDto;
import com.twenty.inhub.boundedContext.answer.controller.dto.QuestionAnswerDto;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import com.twenty.inhub.boundedContext.answer.service.AnswerService;
import com.twenty.inhub.boundedContext.gpt.GptService;
import com.twenty.inhub.boundedContext.gpt.dto.GptResponseDto;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.note.service.NoteService;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    private final QuestionService questionService;

    private final NoteService noteService;

    private final MemberService memberService;

    private final GptService gptService;

    private final Rq rq;


    /**
     * AnswerForm
     * keywords
     */
    // 출제자가 적는 정답
    @AllArgsConstructor
    @Getter
    public static class AnswerCheckForm {
        private String keyword;

        public List<String> getKeywords() {
            return List.of(
                    this.keyword.replace(" ", "")
                            .split(","));
        }
    }

    /**
     * AnswerForm
     * content
     */
    // 정답 적기용 Form
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
        RsData<AnswerCheck> answer = answerService.createAnswer(question.getData(), member,
                createAnswerForm.getContent());

        return rq.redirectWithMsg("/", "객관식 정답 등록완료");
    }

    // 대략적인 모습만 -> Question에 연결될경우 없어질 예정
    @GetMapping("/check/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String CreateCheckAnswer(AnswerCheckForm answerCheckForm) {
        log.info("문제의 정답을 넣어둘 폼 시작");

        if (rq.getMember().getRole() == MemberRole.JUNIOR) {
            return rq.historyBack("접근 권한이 없습니다.");
        }

        return "usr/answer/check/top/create";

    }

    // 문제 출제자가 정답을 넣어 둘때 -> 서술형
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

        RsData<AnswerCheck> answer = answerService.createAnswer(question.getData(), member, answerCheckForm);
        return rq.redirectWithMsg("/", "서술형 정답 등록완료");

    }

    // 서술형 적을 폼 -> 마찬가지로 Question 질문에 연결될때 없어질수 있음.
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

        // 해당문제로 redirect 예정
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
        /*
         * RsData<Answer> answer = answerService.updateAnswer(id, rq.getMember(),
         * answerForm.getContent());
         *
         * if (answer.isFail()) {
         * return rq.historyBack(answer);
         * }
         * return rq.redirectWithMsg("/", answer.getMsg());
         *
         */
        return null;
    }

    // 세션에 임시 저장때 사용됨.
    @PostMapping("/quiz/create")
    @PreAuthorize("isAuthenticated()")
    public String CreateQuizAnswer(@RequestParam(defaultValue = "0") int page, @RequestParam Long id,
                                   createAnswerForm createAnswerForm) {
        RsData<Question> question = questionService.findById(id);
        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");
        if (answerList == null) {
            answerList = new ArrayList<>();
        }

        if (answerList.size() < page) {
            answerList.add(answerService.checkAnswer(question.getData(), rq.getMember(), createAnswerForm.getContent())
                    .getData());
        }
        // 각 페이지 수정할경우
        else {
            log.info("이미 적었던거면 지우고 새로 넣기");
            answerList.remove(page - 1);
            answerList.add(page - 1, answerService
                    .checkAnswer(question.getData(), rq.getMember(), createAnswerForm.getContent()).getData());
        }

        log.info("answerListSize = " + answerList.size());

        rq.getSession().setAttribute("answerList", answerList);

        return "redirect:/question/play?page=%s".formatted(page);
    }

    // 세션 초기화용
    @PostMapping("/reset")
    @PreAuthorize("isAuthenticated()")
    public String ResetAnswer() {
        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");
        answerList.clear();
        rq.getSession().setAttribute("answerList", answerList);

        return "redirect:/";
    }

    // 결과 저장하기 누를때 사용할 로직
    // 결과를 저장하고 세션의 값을 초기화해준다.
    @PostMapping("/quiz/add")
    @PreAuthorize("isAuthenticated()")
    public String AddQuizAnswer() {

        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");
        for (Answer answer : answerList) {
            log.info("점수 : {}", answer.getScore());
            Question question = questionService.findById(answer.getQuestion().getId()).getData();
            answerService.AddAnswer(answer, rq.getMember(), question);
        }

        // 결과 저장완료하면 세션의 값을 지워준다.
        answerList.clear();
        rq.getSession().setAttribute("answerList", answerList);

        return rq.redirectWithMsg("/", "결과 저장완료");
    }

    // 퀴즈 정답 체크 결과 리스트
    // category가 지연로딩이라 가져올수없음. ==> DTO로 변환해서 전달
    // 결과적는 과정을 비동기 처리를 진행해야할거같음.
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public String list() {
        log.info("퀴즈 리스트 제출");
        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");
        Member members = rq.getMember();
        Optional<Member> admin = memberService.findById(1l);
        Member adminMember = admin.get();

        List<CompletableFuture<GptResponseDto>> futures = new ArrayList<>();
        for (int idx = 0; idx < answerList.size(); idx++) {
            Answer answer = answerList.get(idx);
            // 서술형의 경우에만 gpt에게 정답 체크
            if (answer.getContent().length() > 1) {
                QuestionAnswerDto questionAnswerDto = new QuestionAnswerDto();
                questionAnswerDto.setContent(answer.getQuestion().getContent());
                questionAnswerDto.setAnswer(answer.getContent());

                CompletableFuture<GptResponseDto> futureResult = gptService.askQuestion(questionAnswerDto);

                futures.add(futureResult);

            }
        }
        // 모든 비동기 작업이 완료될떄까지 대기
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.thenRun(() -> {
            for (int idx = 0; idx < futures.size(); idx++) {
                log.info("현재 퀴즈 번호 : {}", idx);
                CompletableFuture<GptResponseDto> futureResult = futures.get(idx);
                GptResponseDto gptResponseDto = futureResult.join();

                Answer answer = answerList.get(idx);
                int modifyScore = (int) (answer.getScore() + gptResponseDto.getScore()) / 2;
                log.info("변경된 점수 : {}", modifyScore);
                answerService.updateAnswer(answer, modifyScore, gptResponseDto.getFeedBack());
                log.info("answerListSize = " + answerList.size());
            }

            log.info("비동기 작업 완료 ");



            String domainurl = AppConfig.getDomain();
            log.info("현재 baseUrl :{}",domainurl);
            //baseUrl이용
            String link = domainurl + "/answer/lists"; // 링크 URL을 여기에 적절히 지정해주세요
            String message = "퀴즈 결과가 도착했습니다. 확인하려면 다음 링크를 클릭하세요:<br><a href=\"" + link + "\">퀴즈 결과 보러 가기</a> <br>";
            String message2 = "<br> 현재 퀴즈 결과는 1번밖에 볼 수 없습니다.";

            log.info("쪽지 보내기");
            noteService.sendNote(adminMember, members.getNickname(), "퀴즈 결과가 도착했습니다.", message + message2);
            log.info("쪽지 전송완료");

        });

        log.info("퀴즈 전체 결과 완료");

        return "usr/answer/top/wait";

    }

    // 퀴즈 정답 체크 결과 리스트
    @GetMapping("/lists")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public String lists(Model model) {
        log.info("퀴즈 결과 페이지 응답 요청");
        List<Long> playlist = (List<Long>) rq.getSession().getAttribute("playlist");
        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");

        List<Question> questions = questionService.findByIdList(playlist);
        List<AnswerDto> answerDtos = answerService.convertToDto(questions, answerList);
        model.addAttribute("answerDtos", answerDtos);

        log.info("퀴즈 전체 결과 페이지 응답 완료");

        return "usr/answer/top/list";

    }

    // 상세페이지
    @GetMapping("/result/{id}")
    @PreAuthorize("isAuthenticated()")
    public String resultAnswer(@PathVariable int id, Model model) {

        List<Long> playlist = (List<Long>) rq.getSession().getAttribute("playlist");
        List<Answer> answerList = (List<Answer>) rq.getSession().getAttribute("answerList");

        // 문제번호 및 답 체크
        RsData<Question> question = questionService.findById(playlist.get(id - 1));
        Answer answer = answerList.get(id - 1);
        AnswerCheck answerCheck = answerService.findAnswerCheck(question.getData());

        model.addAttribute("question", question.getData());
        model.addAttribute("answer", answer);
        model.addAttribute("answerCheck", answerCheck);

        return "usr/answer/top/result";
    }

    // 다른 답변들 보기
    @GetMapping("/result/comment/{id}")
    @PreAuthorize("isAuthenticated()")
    public String comment(@PathVariable Long id, Model model) {
        RsData<Question> question = questionService.findById(id);

        Stream<Answer> answerStream = question.getData().getAnswers().stream();
        // 추천순 정렬
        answerStream = answerStream
                .sorted((Comparator.comparing(answer -> ((Answer) answer).getVoter().size())).reversed());

        List<Answer> answers = answerStream.toList();
        model.addAttribute("answers", answers);

        return "usr/answer/top/comment";
    }

    /**
     * delete Answer
     */

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteAnswer(@PathVariable Long id) {
        Answer answer = this.answerService.findByMemberIdAndId(rq.getMember().getId(), id);

        RsData<Answer> CanActDeleteData = answerService.CanDeleteAnswer(rq.getMember(), answer);

        if (CanActDeleteData.isFail()) {
            return rq.historyBack(CanActDeleteData.getMsg());
        }
        answerService.deleteAnswer(answer);

        return rq.historyBack("삭제 완료");

    }

    @GetMapping("/vote/{id}")
    @PreAuthorize("isAuthenticated()")
    public String answerVote(@PathVariable Long id) {
        Answer answer = answerService.getAnswer(id);
        if (answer == null) {
            return rq.historyBack("답이 존재하지않습니다.");
        }

        if (answer.getVoter().contains(rq.getMember())) {
            answerService.removeVoter(answer, rq.getMember());
            return rq.historyBack("추천을 취소하였습니다.");
        }

        answerService.vote(answer, rq.getMember());

        return rq.historyBack("추천 완료");
    }

}
