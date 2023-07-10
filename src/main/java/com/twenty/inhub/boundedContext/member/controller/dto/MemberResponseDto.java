package com.twenty.inhub.boundedContext.member.controller.dto;

import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import lombok.Data;

@Data
public class MemberResponseDto {

    private Long id;
    private String nickname;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
    }
}
