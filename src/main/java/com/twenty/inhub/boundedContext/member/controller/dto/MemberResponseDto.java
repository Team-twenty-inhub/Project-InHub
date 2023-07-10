package com.twenty.inhub.boundedContext.member.controller.dto;

import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.Data;

@Data
public class MemberResponseDto {

    private Long id;
    private String providerType;
    private String nickname;
    private int point;
    private String status;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.providerType = member.getProviderTypeCode();
        this.nickname = member.getNickname();
        this.point = member.getPoint();
        this.status = member.getStatus().toString();
    }
}
