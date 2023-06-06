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
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setCreatedTime(post.getCreatedTime());
        postDto.setPostHits(post.getPostHits());

        if (post.getMember() != null) {
            postDto.setUsername(post.getMember().getUsername());
        }

        return postDto;
    }
}
