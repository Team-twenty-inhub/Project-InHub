package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.jwt.JwtProvider;
import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.question.controller.dto.QuestionResDto;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
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
    private final Rq rq;
    private final JwtProvider jwtProvider;

    @GetMapping("/{id}")
    public RsData findById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("question id = {}", id);
        String accessToken = request.getHeader("Bearer");
        if (!jwtProvider.verify(accessToken))
            throw new IllegalArgumentException("유효하지 않은 token");


        RsData<Question> questionRs = questionService.findById(id);

        if (questionRs.isFail()) {
            throw new IllegalArgumentException("존재하지 않는 id");
        }

        log.info("question 응답 완료");
        return RsData.of(new QuestionResDto(questionRs.getData()));
    }
}
