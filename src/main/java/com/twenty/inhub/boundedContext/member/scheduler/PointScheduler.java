package com.twenty.inhub.boundedContext.member.scheduler;

import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import com.twenty.inhub.boundedContext.member.repository.PointRepository;
import com.twenty.inhub.boundedContext.member.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PointScheduler {

    private final MemberRepository memberRepository;
    private final PointService pointService;

    @Scheduled(cron = "10 * * * * ?")
    public void savePointData() {
        // 모든 회원 조회
        List<Member> members = memberRepository.findAll();

        // 각 회원의 포인트를 조회하여 Point 테이블에 저장
        for (Member member : members) {
            int point = member.getPoint(); // 포인트 계산 로직

            pointService.addPoint(member, point);
        }
    }
}
