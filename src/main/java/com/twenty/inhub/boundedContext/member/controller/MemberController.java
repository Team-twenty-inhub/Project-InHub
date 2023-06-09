package com.twenty.inhub.boundedContext.member.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.service.AnswerService;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.comment.entity.Comment;
import com.twenty.inhub.boundedContext.comment.service.CommentService;
import com.twenty.inhub.boundedContext.device.DeviceService;
import com.twenty.inhub.boundedContext.mail.service.MailService;
import com.twenty.inhub.boundedContext.member.controller.form.*;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.point.service.PointService;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.service.PostService;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import com.twenty.inhub.boundedContext.underline.UnderlineService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PointService pointService;
    private final AnswerService answerService;
    private final PostService postService;
    private final CommentService commentService;
    private final DeviceService deviceService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login() {
        return "usr/member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String joinForm() {
        return "usr/member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid MemberJoinForm form, BindingResult result, HttpServletResponse response) {
        if(result.hasErrors()) {
            return rq.historyBack("올바른 입력 형식이 아닙니다.");
        }

        RsData<Member> rsData = memberService.create(form);

        String deviceId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("deviceId", deviceId);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*365);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        deviceService.createAndSave(deviceId, rsData.getData());

        log.info("회원가입 결과 메세지 = {}", rsData.getMsg());
        log.info("회원가입된 계정 정보 = {}", rsData.getData());

        if(rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/member/login", rsData.getMsg());
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/find/id")
    public String findId() {
        return "usr/member/find/id";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/find/id")
    public String findIdResult(@Valid MemberIdFindForm form, BindingResult errors, Model model) {
        if(errors.hasErrors()) {
            return rq.historyBack("올바른 입력 형식이 아닙니다.");
        }

        RsData<List<String>> rsData = memberService.findMyIds(form.getEmail());

        log.info("아이디 찾기 결과 메세지({}) = {}", form.getEmail(), rsData.getMsg());

        if(rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        log.info("찾은 아이디 목록({}) = {}", form.getEmail(), rsData.getData());

        model.addAttribute("ids", rsData.getData());

        return "usr/member/find/id-result";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/find/pw")
    public String findPw() {
        return "usr/member/find/pw";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/find/pw")
    public String findPwResult(@Valid MemberPwFindForm form, BindingResult errors) {
        if(errors.hasErrors()) {
            return rq.historyBack("올바르지 않은 입력 형식입니다.");
        }

        List<Member> foundByEmail = memberService.findByEmail(form.getEmail());

        boolean exists = foundByEmail.stream()
                .anyMatch(e -> e.getUsername().equals(form.getUsername()));

        if(!exists) {
            return rq.historyBack("일치하는 정보가 없습니다.");
        }

        RsData<?> rsData = memberService.sendTempPw(form.getUsername(), form.getEmail());

        log.info("비밀번호 찾기 결과 메세지({}) = {}", form.getUsername(), rsData.getMsg());

        return rq.redirectWithMsg("/member/login", rsData.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/changePw")
    public String changePwForm() {
        return "usr/member/changePw";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changePw")
    public String changePw(@Valid MemberPwChangeForm form, BindingResult errors) {
        if(errors.hasErrors()) {
            return rq.historyBack("올바르지 않은 입력 형식입니다.");
        }

        Member member = rq.getMember();

        boolean isOk = memberService.checkPassword(member, form.getOriginPassword());

        if(!isOk) {
            return rq.historyBack("기존 비밀번호가 일치하지 않습니다.");
        }

        RsData<?> rsData = memberService.updatePassword(member, form.getPassword());

        rq.getSession().invalidate();

        log.info("비밀번호 변경 결과 메세지({}) = {}", member.getUsername(), rsData.getMsg());

        return rq.redirectWithMsg("/", rsData.getMsg());
    }

    @GetMapping("/{id}")
    public String profile(@PathVariable Long id, Model model) {
        log.info("유저 프로필 정보({})", id);
        Optional<Member> member = memberService.findById(id);

        if(member.isEmpty()) {
            return rq.historyBack("존재하지 않는 회원입니다.");
        }

        model.addAttribute("member", member.get());

        return "usr/member/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/registration")
    public String registrationForm() {
        return "usr/member/registration";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/registration")
    public String registration(String email, HttpServletResponse response) {
        log.info("보안 강화 이메일 등록 = {}", email);

        RsData<Member> rsData = memberService.regEmail(rq.getMember(), email);

        if(rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        String deviceId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("deviceId", deviceId);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*365);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        deviceService.createAndSave(deviceId, rq.getMember());

        return rq.redirectWithMsg("/", rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String myPage(Model model) {
        int rank = memberService.getRanking(rq.getMember());

        log.info("랭킹({}) = {}", rq.getMember().getUsername(), rank);

        model.addAttribute("rank", rank);

        return "usr/member/mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myQuestionList")
    public String myQuestion(Model model) {
        List<Question> questions = rq.getMember().getQuestions();

        log.info("만든 문제 목록({}) = {}", rq.getMember().getUsername(), questions);

        model.addAttribute("questions", questions);

        return "usr/member/question";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profileUpdate")
    public String profileUpdateForm() {
        return "usr/member/update";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profileUpdate")
    public String profileUpdate(@RequestParam("filename") MultipartFile mFile, @Valid MemberUpdateForm form, BindingResult errors) {
        log.info("프로필 수정 = {}", form);

        if(errors.hasErrors()) {
            return rq.historyBack("잘못된 입력 형식입니다.");
        }

        RsData<Member> rsData = memberService.updateProfile(rq.getMember(), form, mFile);

        log.info("프로필 수정 결과 메세지({}) = {}", rq.getMember().getUsername(), rsData.getMsg());

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

        log.info("맞은 문제 목록({}) = {}", rq.getMember().getUsername(), questions);

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

        log.info("틀린 문제 목록({}) = {}", rq.getMember().getUsername(), questions);

        model.addAttribute("questions", questions);

        return "usr/member/incorrect";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPostList")
    public String myPostList(Model model) {
        List<Post> posts = postService.findByMemberId(rq.getMember().getId());

        log.info("작성한 게시글 목록({}) = {}", rq.getMember().getUsername(), posts);

        model.addAttribute("posts", posts);

        return "usr/member/post";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myCommentList")
    public String myCommentList(Model model) {
        List<Comment> comments = commentService.findByMemberId(rq.getMember().getId());

        log.info("작성한 댓글 목록({}) = {}", rq.getMember().getUsername(), comments);

        model.addAttribute("comments", comments);

        return "usr/member/comment";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/graph/point")
    public String pointGraph(Model model) {
        // 일주일 동안의 포인트 변동 데이터를 조회합니다.
        List<Integer> pointData = pointService.getPointDataForGraph(rq.getMember().getId());

        while(pointData.size() < 7) {
            pointData.add(0, 0);
        }

        log.info("포인트 변동 데이터({}) = {}", rq.getMember().getUsername(), pointData);

        // 모델에 포인트 데이터를 추가하여 뷰로 전달합니다.
        model.addAttribute("pointData", pointData);

        return "usr/member/point";
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
}