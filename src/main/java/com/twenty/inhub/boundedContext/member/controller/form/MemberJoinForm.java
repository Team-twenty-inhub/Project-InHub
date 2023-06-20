package com.twenty.inhub.boundedContext.member.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberJoinForm {

    private String username;
    private String password;
    private String email;
    private String nickname;
}
