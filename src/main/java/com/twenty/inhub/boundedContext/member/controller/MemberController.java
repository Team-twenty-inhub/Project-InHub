package com.twenty.inhub.boundedContext.member.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.service.AnswerService;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.member.controller.form.MemberUpdateForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.member.service.PointService;
import com.twenty.inhub.boundedContext.post.dto.PostDto;
import com.twenty.inhub.boundedContext.post.service.PostService;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import com.twenty.inhub.boundedContext.underline.Underline;
import com.twenty.inhub.boundedContext.underline.UnderlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final UnderlineService underlineService;
    private final PointService pointService;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final PostService postService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login() {
        return "usr/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String myPage(Model model) {
        // 일주일 동안의 포인트 변동 데이터를 조회합니다.
        List<Integer> pointData = pointService.getPointDataForGraph(rq.getMember().getId());

        while(pointData.size() < 7) {
            pointData.add(0, 0);
        }

        log.info("pointData = {}", pointData);

        // 모델에 포인트 데이터를 추가하여 뷰로 전달합니다.
        model.addAttribute("pointData", pointData);

        return "usr/member/mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/underlinedQuestionList")
    public String underlinedQuestion(Model model, @RequestParam(defaultValue = "0") int category, @RequestParam(defaultValue = "1") int sortCode) {
        List<Underline> underlines = underlineService.listing(rq.getMember().getUnderlines(), category, sortCode);
        List<Category> categories = categoryService.findAll();

        model.addAttribute("underlines", underlines);
        model.addAttribute("categories", categories);

        return "usr/member/underline";
    }
  
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myQuestionList")
    public String myQuestion(Model model) {
        List<Question> questions = rq.getMember().getQuestions();

        model.addAttribute("questions", questions);

        return "usr/member/question";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profileUpdate")
    public String profileUpdateForm(MemberUpdateForm form) {
        return "usr/member/update";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profileUpdate")
    public String profileUpdate(@RequestParam("filename") MultipartFile mFile, MemberUpdateForm form) {
        RsData<Member> rsData = memberService.updateProfile(rq.getMember(), form, mFile);

        if(rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        return rq.redirectWithMsg("/member/mypage", rsData.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/correctList")
    public String correctList(Model model) {
        List<Answer> list = answerService.findByCorrectAnswer(rq.getMember().getId(), "정답");

        List<Question> questions = list.stream()
                .map(Answer::getQuestion)
                .toList();

        model.addAttribute("questions", questions);

        return "usr/member/correct";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/incorrectList")
    public String incorrectList(Model model) {
        List<Answer> list = answerService.findByCorrectAnswer(rq.getMember().getId(), "오답");

        List<Question> questions = list.stream()
                .map(Answer::getQuestion)
                .toList();

        model.addAttribute("questions", questions);

        return "usr/member/incorrect";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPostList")
    public String myPostList(Model model) {
        List<PostDto> posts = postService.findPost().stream()
                .filter(post -> Objects.equals(post.getAuthorNickname(), rq.getMember().getNickname()))
                .toList();

        model.addAttribute("posts", posts);

        return "usr/member/post";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/members")
    public String members(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "kw", defaultValue = "") String kw,
            @RequestParam(value = "searchBy", defaultValue = "") String searchBy
    ) {
        if(!rq.getMember().isAdmin()) {
            return rq.historyBack("접근 권한이 없습니다.");
        }

        log.info("page = {}, keyword = {}, searchBy = {}", page, kw, searchBy);

        Page<Member> paging = memberService.getMemberList(page, kw, searchBy);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);

        return "adm/members";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/members/updateRole")
    public String roleUpdate(Long id, String role) {
        if(!rq.getMember().isAdmin()) {
            return rq.historyBack("접근 권한이 없습니다.");
        }

        Optional<Member> oMember = memberService.findById(id);

        if(oMember.isEmpty()) {
            return rq.historyBack("해당 유저는 존재하지 않습니다.");
        }

        RsData<Member> rsData = memberService.updateRole(oMember.get(), role);

        log.info("유저 = {}, 변경된 ROLE = {}", oMember.get().getUsername(), role);

        return rq.redirectWithMsg("/member/members", rsData.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/members/updateStatus")
    public String statusUpdate(Long id, String status) {
        if(!rq.getMember().isAdmin()) {
            return rq.historyBack("접근 권한이 없습니다.");
        }

        Optional<Member> oMember = memberService.findById(id);

        if(oMember.isEmpty()) {
            return rq.historyBack("해당 유저는 존재하지 않습니다.");
        }

        RsData<Member> rsData = memberService.updateStatus(oMember.get(), status);

        log.info("유저 = {}, 변경된 STATUS = {}", oMember.get().getUsername(), status);

        return rq.redirectWithMsg("/member/members", rsData.getMsg());
    }

    // 임시
    @GetMapping("/increasePoint")
    public String increasePoint() {
        memberService.increasePoint(rq.getMember());

        return rq.redirectWithMsg("/member/mypage", "포인트를 100 만큼 증가시켰습니다.");
    }

    // 임시
    @GetMapping("/decreasePoint")
    public String decreasePoint() {
        memberService.decreasePoint(rq.getMember());

        return rq.redirectWithMsg("/member/mypage", "포인트를 100 만큼 감소시켰습니다.");
    }
}