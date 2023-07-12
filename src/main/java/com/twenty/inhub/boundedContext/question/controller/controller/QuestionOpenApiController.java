package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.excption.InvalidTokenException;
import com.twenty.inhub.base.excption.NotFoundException;
import com.twenty.inhub.base.jwt.JwtProvider;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.question.controller.dto.QuestionResOpenDto;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/question")
@Tag(name = "문제 관련 API", description = "문제와 관련된 data 를 얻을 수 있습니다.")
@RequiredArgsConstructor
public class QuestionOpenApiController {

    private final QuestionService questionService;
    private final JwtProvider jwtProvider;

    //-- find by id --//
    @GetMapping("/{id}")
    @Operation(description = "id 로 특정 문제 조회하기")
    public QuestionResOpenDto findById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        if (!jwtProvider.verify(request.getHeader("Authorization")))
            throw new InvalidTokenException();
        log.info("question id = {}", id);


        RsData<Question> questionRs = questionService.findById(id);

        if (questionRs.isFail()) {
            throw new NotFoundException("존재하지 않는 id");
        }

        log.info("question 응답 완료");
        return new QuestionResOpenDto(questionRs.getData());
    }

    //-- search question list --//
    @GetMapping("/search")
    @Operation(description = "검색어로 문제 찾기 / input 생략시 모든 data 조회")
    public PageResForm<QuestionResOpenDto> findByQuestion(
            @RequestParam(required = false) String input,
            @RequestParam(defaultValue = "0") int page,
            HttpServletRequest request
    ) {
        if (!jwtProvider.verify(request.getHeader("Authorization")))
            throw new InvalidTokenException();

        log.info("검색어로 question 조회 요청 확인 input = {}", input);

        PageResForm<QuestionResOpenDto> resDto = questionService.findDtoByInput(new SearchForm(2, input, page));

        log.info("검색어로 question 조회 응답 완료 count = {}", resDto.getContents());
        return resDto;
    }
}
