package com.twenty.inhub.boundedContext.post.repository;

import com.twenty.inhub.boundedContext.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedTimeDesc();
    Page<Post> findAll(Pageable pageable);
}
