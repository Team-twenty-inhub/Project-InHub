package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.service.AnswerService;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateListReqDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateListResDto;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionRestController {

    private final CategoryService categoryService;
    private final QuestionService questionService;
    private final MemberService memberService;

    //-- test --//
    @GetMapping()


    //-- 대량 문제 정답 자동 등록 --//
    @PostMapping("/admin/list")
    public RsData update(@RequestBody @Valid UpdateListReqDto dto) {
        log.info("대량 문제 정답 자동 등록 요청 확인 size = {}", dto.getQuestionReqDtoList().size());

        Member member = memberService.findByUsername("admin").get();
        Category category = categoryService.findById(dto.getCategoryId()).getData();

        UpdateListResDto resDto = questionService.createQuestions(dto, member, category);

        int size = resDto.getQuestionResDtoList().size();
        log.info("대량 문제 등록 완료 update count = {}", size);
        return RsData.of("S-1", "count - " + size, resDto);
    }

}
