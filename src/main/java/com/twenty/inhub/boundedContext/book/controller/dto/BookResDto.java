package com.twenty.inhub.boundedContext.book.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResDto {

    private Long id;
    private LocalDateTime createDate;
    private String name;
    private String about;
    private String author;
    private int challenger;
    private int recommend;
    private double accuracy; // 정답률
    private String img;

    public BookResDto(Book book) {
        this.id = book.getId();
        this.createDate = book.getCreateDate();
        this.name = book.getName();
        this.about = book.getAbout();
        this.author = book.getMember().getNickname();
        this.challenger = book.getChallenger();
        this.recommend = book.getRecommend();
        this.accuracy = book.getAccuracy();
        this.img = book.getImg();
    }

    @QueryProjection
    public BookResDto(Long id, LocalDateTime createDate, String name, String about, Member member, int challenger, int recommend, double accuracy, String img) {
        this.id = id;
        this.createDate = createDate;
        this.name = name;
        this.about = about;
        this.author = member.getNickname();
        this.challenger = challenger;
        this.recommend = recommend;
        this.accuracy = accuracy;
        this.img = img;
    }
}
