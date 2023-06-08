package com.twenty.inhub.boundedContext.community.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.community.controller.form.CommunityForm;
import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.repository.CommunityRepository;
import com.twenty.inhub.boundedContext.post.dto.PostDto;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final PostRepository postRepository;

    public Community createCommunity(CommunityForm communityForm) {
        Community community = Community.builder()
                .name(communityForm.getName())
                .build();
        return communityRepository.save(community);
    }

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    public Community getCommunityById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid community id: " + id));
    }

    public Community updateCommunity(Long id, CommunityForm communityForm) {
        Community community = getCommunityById(id);
        community.setName(communityForm.getName());
        return communityRepository.save(community);
    }

    public void deleteCommunity(Long id) {
        Community community = getCommunityById(id);
        communityRepository.delete(community);
    }

    public List<Post> getPostsByCommunityId(Long communityId) {
        Community community = getCommunityById(communityId);
        return community.getPosts();
    }

    public Post createPostInCommunity(Long communityId, PostDto postDto) {
        Community community = getCommunityById(communityId);
        Post post = Post.toSaveEntity(postDto);
        post.setCommunity(community);
        Post savedPost = postRepository.save(post);
        community.getPosts().add(savedPost);
        return savedPost;
    }

    // 다른 비즈니스 로직 메서드들...

}