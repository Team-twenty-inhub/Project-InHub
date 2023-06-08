package com.twenty.inhub.boundedContext.post.repository;

import com.twenty.inhub.boundedContext.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedTimeDesc();

    @Query("SELECT p FROM Post p JOIN FETCH p.member")
    List<Post> findAllWithMember();
}
