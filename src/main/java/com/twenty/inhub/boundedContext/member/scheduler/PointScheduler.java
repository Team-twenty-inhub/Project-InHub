package com.twenty.inhub.boundedContext.member.scheduler;

import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import com.twenty.inhub.boundedContext.member.repository.PointRepository;
import com.twenty.inhub.boundedContext.member.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PointScheduler {

    private final MemberRepository memberRepository;
    private final PointService pointService;

    // 매일 오전 6시 마다 실행
    @Scheduled(cron = "0 0 6 * * *")
    public void savePointData() {
        // 모든 회원 조회
        List<Member> members = memberRepository.findAll();

        // 각 회원의 포인트를 조회하여 Point 테이블에 저장
        for (Member member : members) {
            int point = member.getPoint();

            // 7일 이전 데이터 삭제
            pointService.deleteOver7(member.getId());

            // 당일 데이터 추가
            pointService.createPoint(member, point);
        }
    }
}
