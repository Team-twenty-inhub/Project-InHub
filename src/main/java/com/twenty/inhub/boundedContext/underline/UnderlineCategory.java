package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/underline")
@RequiredArgsConstructor
public class UnderlineCategory {

    private final UnderlineService underlineService;
    private final MemberRepository memberService; // 임시로 Member Repository 메서드 사용함
    private final QuestionService questionService;
    private final Rq rq;


    //-- 밑줄 긋기 생성 --//
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(String about, Long memberId, Long questionId) {
        log.info("밑줄 긋기 생성 요청 확인 question id = {}", questionId);

        Member member = memberService.findById(memberId).get();// 임시로 repository 메서드 사용함
        RsData<Question> questionRs = questionService.findById(questionId);

        if (questionRs.isFail()) {
            log.info("존재하지 않는 문제 msg = {}", questionRs.getMsg());
            return rq.historyBack(questionRs.getMsg());
        }

        RsData<Underline> underlineRs = underlineService.create(about, member, questionRs.getData());

        if (underlineRs.isFail()) {
            log.info("밑줄 긋기 실패 msg = {}", underlineRs.getMsg());
            return rq.historyBack(underlineRs.getMsg());
        }

        log.info("밑줄 긋기 성공 id = {}", underlineRs.getData().getId());
        return rq.historyBack(underlineRs.getMsg());
    }
}
