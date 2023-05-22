package com.twenty.inhub.boundedContext.member.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // 일반 회원가입(임시)
    public RsData<Member> create(String username, String password) {
        if(username.strip().equals("") || password.strip().equals("")) {
            return RsData.of("F-1", "아이디와 비밀번호는 공백일 수 없습니다.");
        }

        Member newMember = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        memberRepository.save(newMember);

        return RsData.of("S-1", "회원가입 성공", newMember);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
