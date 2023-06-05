package com.twenty.inhub.boundedContext.member.repository;

import com.twenty.inhub.boundedContext.member.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByMemberIdOrderByDateTimeAsc(Long memberId);
}
