package com.twenty.inhub.boundedContext.community.service;


import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.repository.CommunityRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityService {
    private final CommunityRepository communityRepository;

    @Transactional
    public Community createCommunity(Community community) {
        return communityRepository.save(community);
    }

    public Community getCommunityById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(EntityExistsException::new);
    }

    @Transactional
    public Community updateCommunity(Long id, Community updatedCommunity) {
        Community existingCommunity = getCommunityById(id);
        existingCommunity.setTitle(updatedCommunity.getTitle());
        existingCommunity.setContent(updatedCommunity.getContent());
        return existingCommunity;
    }

    @Transactional
    public void deleteCommunity(Long id) {
        Community community = getCommunityById(id);
        communityRepository.delete(community);
    }

}
