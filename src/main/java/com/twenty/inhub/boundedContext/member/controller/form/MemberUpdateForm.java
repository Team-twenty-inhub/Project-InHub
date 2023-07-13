package com.twenty.inhub.boundedContext.member.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@ToString
public class MemberUpdateForm {

    @NotBlank
    @Length(min = 2, max = 8, message = "닉네임을 2 ~ 8자 사이로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9ㄱ-힣])[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]{2,8}$", message = "닉네임은 영문, 숫자, 한글로 구성된 2 ~ 8자리입니다.")
    private String nickname;
}
