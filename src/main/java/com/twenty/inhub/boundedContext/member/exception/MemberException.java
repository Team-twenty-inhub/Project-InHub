package com.twenty.inhub.boundedContext.member.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

    private final MemberError errorCode;

    public MemberException(MemberError errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}