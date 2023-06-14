package com.twenty.inhub.boundedContext.post.repository;

import com.twenty.inhub.boundedContext.post.dto.PostDto;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedTimeDesc();
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByBoard(String board, Pageable pageable);
    List<Post> findByMemberId(Long memberId);
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

}
