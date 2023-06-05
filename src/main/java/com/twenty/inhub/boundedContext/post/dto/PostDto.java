package com.twenty.inhub.boundedContext.post.dto;

import com.twenty.inhub.boundedContext.post.entity.Post;
import lombok.*;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdTime;
    private int postHits;

    public static PostDto toPostDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUsername(),
                post.getCreatedTime(),
                post.getPostHits()
        );
    }
}
