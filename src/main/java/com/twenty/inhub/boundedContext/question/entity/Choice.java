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
public class Choice extends BaseEntity {

    private String choice;

    @ManyToOne(fetch = LAZY)
    private Question question;


    //-- create --//
    public static Choice createChoice(String choice) {
        return Choice.builder()
                .choice(choice)
                .build();
    }

    //-- add question --//
    public void addQuestion(Question question) {
        this.question = question;
    }

    //-- update --//
    public Choice updateChoice(String choice) {
        return this.toBuilder()
                .choice(choice)
                .build();
    }

}