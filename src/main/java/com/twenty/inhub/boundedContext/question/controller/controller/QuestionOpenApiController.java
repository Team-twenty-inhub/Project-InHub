package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.jwt.JwtProvider;
import com.twenty.inhub.base.request.Rq;
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
    private final Rq rq;

    //-- find by id --//
    @GetMapping("/{id}")
    @Operation(description = "id 로 특정 문제 조회하기")
    public RsData<QuestionResOpenDto> findById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        String accessToken = request.getHeader("Bearer");
        if (!jwtProvider.verify(accessToken))
            throw new IllegalArgumentException("유효하지 않은 token");
        log.info("question id = {}", id);


        RsData<Question> questionRs = questionService.findById(id);

        if (questionRs.isFail()) {
            throw new IllegalArgumentException("존재하지 않는 id");
        }

        log.info("question 응답 완료");
        return RsData.of(new QuestionResOpenDto(questionRs.getData()));
    }

    //-- search question list --//
    @GetMapping("/search")
    @Operation(description = "검색어로 문제 찾기")
    public RsData<PageResForm<QuestionResOpenDto>> findByQuestion(
            @RequestParam String input,
            @RequestParam int page
//            HttpServletRequest request
    ) {
//        String accessToken = request.getHeader("Bearer");
//        if (!jwtProvider.verify(accessToken))
//            throw new IllegalArgumentException("유효하지 않은 token");
        log.info("검색어로 question 조회 요청 확인 input = {}", input);

        PageResForm<QuestionResOpenDto> resDto = questionService.findDtoByInput(new SearchForm(2, input, page));

        log.info("검색어로 question 조회 응답 완료 count = {}", resDto.getContents());
        return RsData.of(resDto);
    }
}