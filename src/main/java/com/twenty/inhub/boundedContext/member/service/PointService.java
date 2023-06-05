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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public void addPoint(Member member, int value) {
        Point point = Point.builder()
                .member(member)
                .value(value)
                .dateTime(LocalDateTime.now())
                .build();

        pointRepository.save(point);
    }

    public List<Integer> getPointDataForGraph(Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Point> points = pointRepository.findByMemberIdAndDateTimeBetweenOrderByDateTimeAsc(memberId, startDateTime, endDateTime);
        List<Integer> pointValues = new ArrayList<>();
        for (Point point : points) {
            pointValues.add(point.getValue());
        }
        return pointValues;
    }
}
