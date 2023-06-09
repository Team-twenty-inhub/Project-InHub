package com.twenty.inhub.boundedContext.answer.event;

import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AnswerCheckPointEvent extends ApplicationEvent {

    private final Member member;
    private final int point;

    public AnswerCheckPointEvent(Object source, Member member, int point) {
        super(source);
        this.member = member;
        this.point = point;
    }
}
