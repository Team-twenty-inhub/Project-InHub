package com.twenty.inhub.boundedContext.member.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.controller.form.MemberUpdateForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.entity.Question;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final UnderlineService underlineService;
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
    public String underlinedQuestion(Model model, @RequestParam(defaultValue = "0") int category, @RequestParam(defaultValue = "1") int sortCode) {
        List<Underline> underlines = underlineService.listing(rq.getMember().getUnderlines(), category, sortCode);

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profileUpdate")
    public String profileUpdateForm(MemberUpdateForm form) {
        return "/usr/member/update";
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
    @GetMapping("/members")
    public String members(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        log.info("page = {}", page);

        Page<Member> paging = memberService.getMemberList(page);

        model.addAttribute("paging", paging);

        return "/adm/members";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/members/updateRole")
    public String roleUpdate(Long id, String role) {
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
        Optional<Member> oMember = memberService.findById(id);

        if(oMember.isEmpty()) {
            return rq.historyBack("해당 유저는 존재하지 않습니다.");
        }

        RsData<Member> rsData = memberService.updateStatus(oMember.get(), status);

        log.info("유저 = {}, 변경된 STATUS = {}", oMember.get().getUsername(), status);

        return rq.redirectWithMsg("/member/members", rsData.getMsg());
    }
}