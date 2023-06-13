package com.twenty.inhub.boundedContext.comment.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.comment.dto.CommentDto;
import com.twenty.inhub.boundedContext.comment.entity.Comment;
import com.twenty.inhub.boundedContext.comment.repository.CommentRepository;
import com.twenty.inhub.boundedContext.comment.service.CommentService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final Rq rq;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@ModelAttribute("commentDto") CommentDto commentDto, @RequestParam("postId") Long postId) {
        Member member = rq.getMember();
        Post post = postService.getPost(postId);
        commentService.createComment(commentDto, member, post);
        return rq.redirectWithMsg("/post/view/" + postId, RsData.of("S-60", "댓글이 생성되었습니다"));
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updateComment(@PathVariable("id") Long id, @RequestParam("content") String content) {
        Long postId = commentService.updateComment(id, content);
        if (postId != null) {
            return rq.redirectWithMsg("/post/view/" + postId, RsData.of("S-63", "댓글이 수정 되었습니다"));
        } else {
            return rq.redirectBackWithMsg("댓글 수정에 실패했습니다");
        }    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return rq.redirectBackWithMsg("댓글이 삭제되었습니다");
    }
}

