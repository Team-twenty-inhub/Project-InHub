package com.twenty.inhub.boundedContext.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
