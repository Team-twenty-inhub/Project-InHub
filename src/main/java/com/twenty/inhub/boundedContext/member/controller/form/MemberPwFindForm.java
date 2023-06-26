package com.twenty.inhub.boundedContext.member.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class MemberPwFindForm {
    @NotBlank
    @Length(min = 4, max = 12, message = "아이디를 4 ~ 12자 사이로 입력해주세요.")
    @Pattern(regexp = "^[A-za-z0-9]{4,12}$", message = "아이디는 영문, 숫자만 가능하며 4 ~ 12자리까지 가능합니다.")
    private String username;
    @NotBlank
    @Email
    private String email;
}
