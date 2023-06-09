package com.twenty.inhub.boundedContext.answer.event;

import com.twenty.inhub.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class AnswerEventListener {

    private final MemberService memberService;

    @EventListener
    public void listen(AnswerCheckPointEvent event) {
//        memberService.increasePoint(event.getMember(), event.getPoint());
    }
}
