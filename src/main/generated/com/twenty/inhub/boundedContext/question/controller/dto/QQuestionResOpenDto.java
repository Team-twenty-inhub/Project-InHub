package com.twenty.inhub.boundedContext.question.controller.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.twenty.inhub.boundedContext.question.controller.dto.QQuestionResOpenDto is a Querydsl Projection type for QuestionResOpenDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QQuestionResOpenDto extends ConstructorExpression<QuestionResOpenDto> {

    private static final long serialVersionUID = -1172879486L;

    public QQuestionResOpenDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<? extends com.twenty.inhub.boundedContext.member.entity.Member> member, com.querydsl.core.types.Expression<? extends com.twenty.inhub.boundedContext.category.Category> category, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Integer> difficulty, com.querydsl.core.types.Expression<Integer> challenger, com.querydsl.core.types.Expression<java.time.LocalDateTime> createDate) {
        super(QuestionResOpenDto.class, new Class<?>[]{long.class, String.class, com.twenty.inhub.boundedContext.member.entity.Member.class, com.twenty.inhub.boundedContext.category.Category.class, String.class, int.class, int.class, java.time.LocalDateTime.class}, id, name, member, category, content, difficulty, challenger, createDate);
    }

}

