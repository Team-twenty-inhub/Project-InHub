package com.twenty.inhub.boundedContext.question.controller.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateListReqDto {

    private Long categoryId;
    private List<QuestionReqDto> reqDtoList;
}
