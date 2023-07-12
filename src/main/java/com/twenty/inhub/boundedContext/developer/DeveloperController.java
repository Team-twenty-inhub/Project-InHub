package com.twenty.inhub.boundedContext.developer;

import com.twenty.inhub.base.appConfig.AppConfig;
import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.member.entity.MemberStatus;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.note.controller.form.NoteSendForm;
import com.twenty.inhub.boundedContext.note.entity.Note;
import com.twenty.inhub.boundedContext.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.twenty.inhub.boundedContext.member.entity.MemberRole.ADMIN;
import static com.twenty.inhub.boundedContext.member.entity.MemberStatus.ING;

@Slf4j
@Controller
@RequestMapping("/developer")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;
    private final MemberService memberService;
    private final NoteService noteService;
    private final Rq rq;

    //-- 개발자 페이지 홈 --//
    @GetMapping("/menu")
    public String menu() {
        log.info("open api 매뉴 페이지 응답 완료");

        return "usr/developer/top/menu";
    }

    //-- access key 상세페이지 --//
    @GetMapping("/access-key")
    @PreAuthorize("isAuthenticated()")
    public String accessKey(NoteSendForm form) {

        log.info("access key 상세페이지 요청 확인");

        form.setReceiver("admin");
        form.setTitle("access token 요청");

        log.info("access key 상세페이지 응답 완료");

        return "usr/developer/top/access";
    }

    //-- access key 신청 --//
    @PostMapping("/access-key")
    @PreAuthorize("isAuthenticated()")
    public String accessKeyRequest(NoteSendForm form) {

        log.info("access key 요청 쪽지 보내기 요청 확인");
        Member member = rq.getMember();

        String link = "<br><a href=\""
                + AppConfig.getDomain() + "/developer/access/" + member.getNickname() +
                "\">access key 발급 승인</a>";

        RsData<Note> noteRs = noteService.sendNote(
                member.getNickname(),
                form.getReceiver(),
                form.getTitle(),
                form.getContent() + link
        );

        if (noteRs.isFail()) {
            log.info("요청 실패 msg = {}", noteRs.getMsg());
            return rq.historyBack(noteRs.getMsg());
        }

        return rq.redirectWithMsg("/developer/access-key", "요청이 완료되었습니다.");
    }

    @GetMapping("/access/{nickname}")
    @PreAuthorize("isAuthenticated()")
    public String access(
            @PathVariable String nickname
    ) {
        log.info("access token 발급 허가 request nickname = {}", nickname);

        if (rq.getMember().getRole() != ADMIN) {
            log.info("접근 권한 없음");
            return rq.historyBack("접근 권한이 없습니다.");
        }

        Member member = memberService.findByNickname(nickname).orElse(null);

        if (member == null) {
            log.info("존재하지 않는 username");
            return rq.historyBack("존재하지 않는 회원입니다.");
        }

        memberService.updateStatus(member, "ING");
        String content = "<a href=\""
                + AppConfig.getDomain() + "/developer/create" +
                "\">access key 확인하기</a>";

        noteService.sendNote("admin", nickname, "access token 승인이 완료되었습니다.", content);

        log.info("access key 발급 승인 허가 member id = {}", member.getId());
        return rq.historyBack("승인 허가");
    }


    //-- access key 발급 --//
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String join(Model model) {
        log.info("access key 발급 요청 확인");

        Member member = rq.getMember();
        if (member.getStatus() != ING){
            log.info("접근 권한 없음");
            return rq.historyBack("페이지가 만료되었습니다.");
        }

        String token = developerService.create(member);

        model.addAttribute("token", token);
        memberService.updateStatus(member, "STOP");

        log.info("access token 발급 완료 member id = {}", member.getId());
        return "usr/developer/top/create";
    }

    //-- stats 폼 --//
    @GetMapping("/stats")
    public String stats(
            @RequestParam String username,
            Model model
    ) {
        log.info("in hub stats 조회 요청 확인");

        Optional<Member> byUsername = memberService.findByUsername(username);

        if (!byUsername.isPresent()) {
            log.info("존재하지 않는 username");
            return "stats/stats";
        }

        Member member = byUsername.get();
        model.addAttribute("member", member);

        log.info("stats 응답 완료");
        return "stats/stats";
    }

}
