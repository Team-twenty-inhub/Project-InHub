package com.twenty.inhub.boundedContext.community.service;


import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.repository.CommunityRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;

    @Transactional // 새로운 Community 생성 및 저장
    public Community createCommunity(Community community) {
        return communityRepository.save(community);
    }

    @Transactional(readOnly = true) // 주어진 ID에 해당하는 Community 조회
    public Community getCommunityById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(EntityExistsException::new);
    }

    @Transactional // 주어진 ID에 해당하는 Community 업데이트
    public Community updateCommunity(Long id, Community updatedCommunity) {
        Community existingCommunity = getCommunityById(id);
        existingCommunity.setTitle(updatedCommunity.getTitle());
        existingCommunity.setContent(updatedCommunity.getContent());
        return existingCommunity;
    }

    @Transactional // 주어진 ID에 해당하는 Community 삭제
    public void deleteCommunity(Long id) {
        Community community = getCommunityById(id);
        communityRepository.delete(community);
    }

}
