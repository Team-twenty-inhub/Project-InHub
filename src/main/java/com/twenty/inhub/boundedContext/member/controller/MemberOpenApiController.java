package com.twenty.inhub.boundedContext.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/member")
@Tag(name = "회원 관련 API", description = "회원과 관련된 data 를 얻을 수 있습니다.")
@RequiredArgsConstructor
public class MemberOpenApiController {
}
