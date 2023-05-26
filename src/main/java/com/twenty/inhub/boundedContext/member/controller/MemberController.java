package com.twenty.inhub.boundedContext.member.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.underline.Underline;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profileUpdate")
    public String profileUpdateForm() {
        return "/usr/member/update";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profileUpdate")
    public String profileUpdate(@RequestParam("filename") MultipartFile mFile) {
        /*
        File 객체는 절대 경로만 사용 가능한데,
        우리는 협업중이므로 각자의 컴퓨터마다 저장되는 공간이 다르기에 상대경로가 필요하다.
        하지만 상대경로를 구해도 File 객체에 적용이 불가능하니
        결국 각자 컴퓨터마다 static 디렉토리의 절대경로가 필요하다.
        현재 MemberController 클래스의 저장된 절대경로를 구하고
        "out/" 문자열을 기준으로 자르면 앞에 있는 문자열이 해당 컴퓨터의 절대경로 일부분이 나온다.
        그 경로 뒤에 원하는 저장공간의 절대주소를 붙여주면, 해당 컴퓨터에 저장할 공간인 최종 절대경로를 구할 수 있다.
         */
        String path = MemberController.class.getResource("").getPath();
        String[] pathBits = path.split("out/");
        String uploadPath = pathBits[0] + "src/main/resources/static/images/profile/"; // 프로필 사진들 모아두는 폴더
        String username = rq.getMember().getUsername();
        Member member = memberService.findByUsername(username).get();

        try {
            if (member.getProfileImg() != null) { // 이미 프로필 사진이 있을경우
                File file = new File(uploadPath + member.getProfileImg()); // 경로 + 유저 프로필사진 이름을 가져와서
                file.delete(); // 원래파일 삭제
            }
            mFile.transferTo(new File(uploadPath + mFile.getOriginalFilename()));  // 경로에 업로드
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        memberService.updateProfileImg(member, mFile.getOriginalFilename()); // 프로필 사진이름 db에 update

        return rq.redirectWithMsg("/member/profileUpdate", "업로드 완료");
    }
}
