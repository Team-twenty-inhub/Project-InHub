package com.twenty.inhub.boundedContext.question.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.twenty.inhub.boundedContext.category.Category;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionResOpenDto {

    private Long id;
    private String name;
    private String author;
    private String category;
    private List<String> tag;
    private String content;
    private int difficulty;
    private int challenger;
    private LocalDateTime createDate;

    public QuestionResOpenDto(Question question) {
        List<String> tagList = new ArrayList<>();
        List<Tag> tags = question.getTags();
        for (Tag tag : tags) tagList.add(tag.getTag());

        this.id = question.getId();
        this.name = question.getName();
        this.author = question.getMember().getNickname();
        this.category = question.getCategory().getName();
        this.tag = tagList;
        this.content = question.getContent();
        this.difficulty = question.getDifficulty();
        this.challenger = question.getChallenger();
        this.createDate = question.getCreateDate();
    }

    @QueryProjection
    public QuestionResOpenDto(Long id, String name, Member member, Category category, String content, int difficulty, int challenger, LocalDateTime createDate) {
        this.id = id;
        this.name = name;
        this.author = member.getNickname();
        this.category = category.getName();
        this.content = content;
        this.difficulty = difficulty;
        this.challenger = challenger;
        this.createDate = createDate;
    }
}
