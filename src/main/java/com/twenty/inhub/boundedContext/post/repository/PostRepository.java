package com.twenty.inhub.boundedContext.post.repository;

import com.twenty.inhub.boundedContext.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
