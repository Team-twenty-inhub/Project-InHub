package com.twenty.inhub.boundedContext.question.entity;

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
public class Tag extends BaseEntity {

    private String tag;

    @ManyToOne(fetch = LAZY)
    private Question question;


    //-- create --//
    public static Tag createTag(String tag) {
        return Tag.builder()
                .tag(tag)
                .build();
    }

    //-- add question --//
    public void addQuestion(Question question) {
        this.question = question;
    }

    //-- update --//
    public Tag updateTag(String tag) {
        return this.toBuilder()
                .tag(tag)
                .build();
    }

}
