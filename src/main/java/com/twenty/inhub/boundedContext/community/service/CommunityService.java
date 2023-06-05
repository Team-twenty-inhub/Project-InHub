package com.twenty.inhub.boundedContext.community.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
   
    @Transactional // 새로운 Community 생성 및 저장
    public RsData<Community> createCommunity(Community community) {
        Community createdCommunity = communityRepository.save(community);
        return RsData.of("S-1", "성공", createdCommunity);
    }

    @Transactional(readOnly = true) // 주어진 ID에 해당하는 Community 조회
    public RsData<Community> getCommunityById(Long id) {
        Community retrievedCommunity = communityRepository.findById(id)
                .orElse(null);

        if (retrievedCommunity != null) {
            return RsData.of("S-1", "조회 완료", retrievedCommunity);
        } else {
            return RsData.of("F-1", "데이터를 찾을 수 없습니다.");
        }
    }

    @Transactional(readOnly = true) // 모든 Community 조회
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }
}