package com.twenty.inhub.boundedContext.member.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberJoinForm {
    @NotBlank
    @Size(min = 4, message = "아이디를 4자 이상 입력해주세요.")
    private String username;
    @NotBlank
    @Size(min = 4, message = "비밀번호를 4자 이상 입력해주세요.")
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 4, message = "닉네임을 4자 이상 입력해주세요.")
    private String nickname;
}
