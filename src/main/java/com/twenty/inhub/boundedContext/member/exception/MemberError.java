package com.twenty.inhub.boundedContext.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberError {
    NOT_VALID_MEMBER_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 ID 입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}