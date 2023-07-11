package com.twenty.inhub.boundedContext.book.controller.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.twenty.inhub.boundedContext.book.controller.dto.QBookResDto is a Querydsl Projection type for BookResDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBookResDto extends ConstructorExpression<BookResDto> {

    private static final long serialVersionUID = 1853232434L;

    public QBookResDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<java.time.LocalDateTime> createDate, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> about, com.querydsl.core.types.Expression<? extends com.twenty.inhub.boundedContext.member.entity.Member> member, com.querydsl.core.types.Expression<Integer> challenger, com.querydsl.core.types.Expression<Integer> recommend, com.querydsl.core.types.Expression<Double> accuracy, com.querydsl.core.types.Expression<String> img) {
        super(BookResDto.class, new Class<?>[]{long.class, java.time.LocalDateTime.class, String.class, String.class, com.twenty.inhub.boundedContext.member.entity.Member.class, int.class, int.class, double.class, String.class}, id, createDate, name, about, member, challenger, recommend, accuracy, img);
    }

}

