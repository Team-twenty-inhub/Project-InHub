package com.twenty.inhub.boundedContext.member.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class MemberJoinForm {
    @NotBlank
    @Length(min = 4, max = 12, message = "아이디를 4 ~ 12자 사이로 입력해주세요.")
    @Pattern(regexp = "^[A-za-z0-9]{4,12}$", message = "아이디는 영문, 숫자만 가능하며 4 ~ 12자리까지 가능합니다.")
    private String username;
    @NotBlank
    @Length(min = 8, max = 15, message = "비밀번호를 8 ~ 15자 사이로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", message = "비밀번호는 영문, 숫자, 특수기호를 꼭 포함하는 8 ~ 15자리입니다.")
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 2, max = 8, message = "닉네임을 2 ~ 8자 사이로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z0-9ㄱ-힣])[a-z0-9가-힣ㄱ-ㅎㅏ-ㅣ]{2,8}$", message = "닉네임은 영문, 숫자, 한글로 구성된 2 ~ 8자리입니다.")
    private String nickname;
}
