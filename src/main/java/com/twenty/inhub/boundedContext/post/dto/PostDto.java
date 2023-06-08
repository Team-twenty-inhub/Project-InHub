package com.twenty.inhub.boundedContext.post.dto;

import com.twenty.inhub.boundedContext.member.entity.Member;
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
    private LocalDateTime createdTime;
    private int postHits;
    private String authorNickname;
    private Member author;



    public static PostDto toPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setCreatedTime(post.getCreatedTime());
        postDto.setPostHits(post.getPostHits());
        if (post.getMember() != null) {
            postDto.setAuthorNickname(post.getMember().getNickname());
            postDto.setAuthor(post.getMember());
        }

        return postDto;
    }
}
