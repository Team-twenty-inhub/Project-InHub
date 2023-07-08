package com.twenty.inhub.boundedContext.question.controller.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/question")
@Tag(name = "문제 관련 API", description = "문제와 관련된 data 를 얻을 수 있습니다.")
@RequiredArgsConstructor
public class QuestionOpenApiController {


}
