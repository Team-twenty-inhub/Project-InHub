package com.twenty.inhub.boundedContext.question.controller.controller;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.category.CategoryService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.CategoryReqDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateCategoryDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateListReqDto;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.UpdateListResDto;
import com.twenty.inhub.boundedContext.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Hidden
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestionRestController {

    private final CategoryService categoryService;
    private final QuestionService questionService;
    private final MemberService memberService;

    //-- test --//
    @GetMapping("/test")
    public RsData test() {
        log.info("통신 성공");
        return RsData.of("S-1", "통신 성공");
    }
    @PostMapping("/test")
    public RsData test1() {
        log.info("통신 성공");
        return RsData.of("S-1", "통신 성공");
    }


    //-- Category 등록 -- //
    @PostMapping("/category")
    public RsData<List<Long>> categoryUpdate(@RequestBody @Valid UpdateCategoryDto dto) {
        List<CategoryReqDto> categories = dto.getCategories();
        log.info("카테고리 등록 요청 확인 count = {}", categories.size());

        RsData<List<Long>> listRsData = categoryService.create(categories);

        log.info("Categories 등록 완료 count = {}", listRsData.getData().size());
        return listRsData;
    }


    //-- 대량 문제 정답 자동 등록 --//
    @PostMapping("/create/list")
    public RsData update(@RequestBody UpdateListReqDto dto) {
        log.info("대량 문제 정답 자동 등록 요청 확인 size = {}", dto.getReqDtoList().size());

        Member member = memberService.findByUsername("admin").get();
        Category category = categoryService.findById(dto.getCategoryId()).getData();

        UpdateListResDto resDto = questionService.createQuestions(dto, member, category);

        int size = resDto.getReqDtoList().size();
        log.info("대량 문제 등록 완료 update count = {}", size);
        return RsData.of("S-1", "count - " + size, resDto);
    }


}
