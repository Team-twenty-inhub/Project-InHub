package com.twenty.inhub.boundedContext.question.controller.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateListResDto {

    private int count;
    private List<QuestionResDto> reqDtoList;
}
