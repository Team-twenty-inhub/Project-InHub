package com.twenty.inhub.boundedContext.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberError {
    //-- 이곳에 추가하고 싶은 ErrorCode 를 선언해주세요. --//
//    NOT_VALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰 입니다."),
//    NOT_VALID_MEMBER_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 ID 입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}