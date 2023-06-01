package com.twenty.inhub.boundedContext.post.controller;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.service.CommunityService;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/community/{communityId}/post")
@RequiredArgsConstructor
public class PostController {
    private final CommunityService communityService;
    private final PostService postService;

    // 게시글 생성
    @PostMapping("/create")
    public RsData<Post> createPost(@RequestBody Post post, @RequestParam Long communityId) {
        RsData<Community> communityRs = communityService.getCommunityById(communityId);

        if (communityRs.isSuccess()) {
            Community community = communityRs.getData();
            post.setCommunity(community);
            RsData<Post> result = postService.createPost(post);
            return result;
        } else {
            return RsData.of("F-1", "게시판을 찾을 수 없습니다.");
        }
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public RsData<Post> getPostById(@PathVariable Long postId) {
        RsData<Post> result = postService.getPostById(postId);
        return result;
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public RsData<Post> updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        RsData<Post> result = postService.updatePost(postId, updatedPost);
        return result;
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public RsData<Void> deletePost(@PathVariable Long postId) {
        RsData<Void> result = postService.deletePost(postId);
        return result;
    }
}
