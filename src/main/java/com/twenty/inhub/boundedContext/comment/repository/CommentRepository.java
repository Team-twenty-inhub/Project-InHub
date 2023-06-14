package com.twenty.inhub.boundedContext.comment.repository;

import com.twenty.inhub.boundedContext.comment.entity.Comment;
import com.twenty.inhub.boundedContext.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByMemberId(Long memberId);

    int countByPost(Post post);

    List<Comment> findByPost(Post post);
}
