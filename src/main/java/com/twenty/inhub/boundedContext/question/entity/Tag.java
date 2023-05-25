package com.twenty.inhub.boundedContext.question.entity;

import com.twenty.inhub.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Tag extends BaseEntity {
}
