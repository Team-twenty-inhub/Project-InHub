package com.twenty.inhub.boundedContext.comment.entity;

import com.twenty.inhub.boundedContext.comment.dto.CommentDto;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    @CreationTimestamp
    private LocalDateTime createdTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "member_id")
    private Member member;

    public boolean isCreatedBy(Member member) {
        return member != null && member.equals(this.member);
    }

    public static Comment toSaveEntity(CommentDto commentDto, Member member, Post post) {
        Comment build = Comment.builder()
                .content(commentDto.getContent())
                .member(member)
                .post(post)
                .build();
        member.getComments().add(build);
        return build;
    }
}
