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
        RsData rsData = commentService.updateComment(id, content, rq.getMember());
        if (rsData.isSuccess()) {
            Long postId = (Long) rsData.getData();
            return rq.redirectWithMsg("/post/view/" + postId, RsData.of("S-63", "댓글이 수정 되었습니다"));
        }
        else {
            return rq.historyBack("이 댓글을 수정할 권한이 없습니다");
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@PathVariable("id") Long id) {
        Member member = rq.getMember();
        RsData rsData = commentService.deleteComment(id, member);
        if (rsData.isSuccess()){
            return rq.redirectBackWithMsg("댓글이 삭제 되었습니다,");
        }
        else {
            return rq.historyBack("이 댓글을 삭제할 권한이 없습니다.");
        }
    }
}
