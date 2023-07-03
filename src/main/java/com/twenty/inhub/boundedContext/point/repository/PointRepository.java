package com.twenty.inhub.boundedContext.point.repository;

import com.twenty.inhub.boundedContext.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByMemberIdOrderByDateTimeAsc(Long memberId);
}
