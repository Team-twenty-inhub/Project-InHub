package com.twenty.inhub.boundedContext.member.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.underline.Underline;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login() {
        return "/usr/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String myPage() {
        return "/usr/member/mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/underlinedQuestionList")
    public String underlinedQuestion(Model model) {
        List<Underline> underlines = rq.getMember().getUnderlines();

        model.addAttribute("underlines", underlines);

        return "/usr/member/underline";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myQuestionList")
    public String myQuestion(Model model) {
        List<Question> questions = rq.getMember().getQuestions();

        model.addAttribute("questions", questions);

        return "/usr/member/question";
    }
}
