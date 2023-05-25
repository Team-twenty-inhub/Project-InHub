package com.twenty.inhub.boundedContext.community.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            return RsData.of("S-1","조회 완료", retrievedCommunity);
        } else {
            return RsData.of("F-1","데이터를 찾을 수 없습니다.");
        }
    }

    @Transactional // 주어진 ID에 해당하는 Community 업데이트
    public RsData<Community> updateCommunity(Long id, Community updatedCommunity) {
        Community existingCommunity = getCommunityById(id).getData();

        if (existingCommunity != null) {
            existingCommunity.setTitle(updatedCommunity.getTitle());
            existingCommunity.setContent(updatedCommunity.getContent());
            Community modifiedCommunity = communityRepository.save(existingCommunity);
            return RsData.of("S-1","수정 성공", modifiedCommunity);
        } else {
            return RsData.of("F-1","데이터를 찾을 수 없습니다.");
        }

    @Transactional // 주어진 ID에 해당하는 Community 삭제
    public RsData<Void> deleteCommunity(Long id) {
        Community existingCommunity = getCommunityById(id).getData();

        if (existingCommunity != null) {
            communityRepository.delete(existingCommunity);
            return RsData.of("S-1","삭제 성공");
        } else {
            return RsData.of("F-1","데이터를 찾을 수 없습니다.");
        }
    }
}