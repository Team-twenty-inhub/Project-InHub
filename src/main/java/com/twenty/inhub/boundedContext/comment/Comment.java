package com.twenty.inhub.boundedContext.comment;

import com.twenty.inhub.boundedContext.member.Member;
import com.twenty.inhub.boundedContext.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @CreatedDate
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
