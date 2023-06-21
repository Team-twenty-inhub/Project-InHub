package com.twenty.inhub.boundedContext.member.repository;

import com.twenty.inhub.boundedContext.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    Optional<Member> findByNickname(String nickname);
    Page<Member> findAll(Pageable pageable);
    Page<Member> findAll(Specification spec, Pageable pageable);
    List<Member> findByEmail(String email);
}
