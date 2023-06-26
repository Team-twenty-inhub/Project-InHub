package com.twenty.inhub.boundedContext.member.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class MemberPwChangeForm {
    @NotBlank
    private String originPassword;
    @NotBlank
    @Length(min = 8, max = 15, message = "비밀번호를 8 ~ 15자 사이로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", message = "비밀번호는 영문, 숫자, 특수기호를 꼭 포함하는 8 ~ 15자리입니다.")
    private String password;
}
