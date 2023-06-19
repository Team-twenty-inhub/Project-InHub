package com.twenty.inhub.boundedContext.answer.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Keyword extends BaseEntity {
    private String keyword;

    @ManyToOne(fetch = LAZY)
    private AnswerCheck answerCheck;

    //create
    public static Keyword createKeyword(String keyword){
        return Keyword.builder()
                .keyword(keyword)
                .build();

    }
}
