package com.twenty.inhub.boundedContext.member.service;

import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.Point;
import com.twenty.inhub.boundedContext.member.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public void createPoint(Member member, int value) {
        Point point = Point.builder()
                .member(member)
                .value(value)
                .dateTime(LocalDateTime.now())
                .build();

        pointRepository.save(point);
    }

    /*
    memberId와 일치하는 Point 리스트를 가져와,
    포인트 값만 추출하여 List<Integer> 형태로 반환하는 메소드
     */
    public List<Integer> getPointDataForGraph(Long memberId) {
        List<Point> points = pointRepository.findByMemberIdOrderByDateTimeAsc(memberId);

        return points.stream()
                .map(Point::getValue)
                .collect(Collectors.toList());
    }

    /*
    memberId와 일치하는 Point 리스트의 사이즈가 7이라면,
    가장 오래된 Point를 삭제해주는 메소드
     */
    @Transactional
    public void deleteOver7(Long memberId) {
        List<Point> pointList = pointRepository.findByMemberIdOrderByDateTimeAsc(memberId);

        if(pointList.size() == 7) {
            Point point = pointList.get(0);
            pointRepository.delete(point);
        }
    }
}
