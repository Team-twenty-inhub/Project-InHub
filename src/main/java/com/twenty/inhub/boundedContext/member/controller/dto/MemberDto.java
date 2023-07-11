package com.twenty.inhub.boundedContext.member.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @JsonProperty("user_id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("profile_img")
    private String profileImg;

    public static MemberDto fromUser(Member user) {
        MemberDto memberDto = MemberDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .createdAt(user.getCreateDate())
                .updatedAt(user.getModifyDate())
                .profileImg(user.getProfileImg())
                .build();

        return memberDto;
    }
}
