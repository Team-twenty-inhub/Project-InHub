package com.twenty.inhub.boundedContext.member.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberIdFindForm {
    @NotBlank
    @Email
    private String email;
}
