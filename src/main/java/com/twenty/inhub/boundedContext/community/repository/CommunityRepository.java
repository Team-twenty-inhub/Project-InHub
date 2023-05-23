package com.twenty.inhub.boundedContext.community.repository;

;
import com.twenty.inhub.boundedContext.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
