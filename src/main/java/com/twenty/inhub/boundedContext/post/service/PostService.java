package com.twenty.inhub.boundedContext.post.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.post.dto.PostDto;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final List<PostDto> postDtoList = new ArrayList<>();

    public void createPost(PostDto postDto, String username) {
        Post post = Post.toSaveEntity(postDto);
        post.setUsername(username);
        Post savedPost = postRepository.save(post);
        postDtoList.add(PostDto.toPostDto(savedPost));
    }

    public List<PostDto> findPost() {
        postDtoList.clear();
        for (Post post : postRepository.findAll()) {
            postDtoList.add(PostDto.toPostDto(post));
        }
        return postDtoList;
    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
