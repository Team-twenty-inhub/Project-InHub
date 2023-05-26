package com.twenty.inhub.boundedContext.post.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional // 새로운 게시글 생성 및 저장
    public RsData<Post> createPost(Post post) {
        Post createdPost = postRepository.save(post);
        return RsData.of("S-1", "성공", createdPost);
    }

    @Transactional(readOnly = true) // 주어진 ID에 해당하는 게시글 조회
    public RsData<Post> getPostById(Long id) {
        Post retrievedPost = postRepository.findById(id).orElse(null);

        if (retrievedPost != null) {
            return RsData.of("S-1", "조회 완료", retrievedPost);
        } else {
            return RsData.of("F-1", "데이터를 찾을 수 없습니다.");
        }
    }

    @Transactional // 주어진 ID에 해당하는 게시글 업데이트
    public RsData<Post> updatePost(Long id, Post updatedPost) {
        Post existingPost = postRepository.findById(id).orElse(null);

        if (existingPost != null) {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
            existingPost.setImage(updatedPost.getImage());
            existingPost.setFile(updatedPost.getFile());
            Post modifiedPost = postRepository.save(existingPost);
            return RsData.of("S-1", "수정 성공", modifiedPost);
        } else {
            return RsData.of("F-1", "데이터를 찾을 수 없습니다.");
        }
    }

    @Transactional // 주어진 ID에 해당하는 게시글 삭제
    public RsData<Void> deletePost(Long id) {
        Post existingPost = postRepository.findById(id).orElse(null);

        if (existingPost != null) {
            postRepository.delete(existingPost);
            return RsData.of("S-1", "삭제 성공");
        } else {
            return RsData.of("F-1", "데이터를 찾을 수 없습니다.");
        }
    }
}
