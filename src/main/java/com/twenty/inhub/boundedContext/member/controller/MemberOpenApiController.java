package com.twenty.inhub.boundedContext.member.controller;

import com.twenty.inhub.base.excption.InvalidTokenException;
import com.twenty.inhub.base.excption.NotFoundException;
import com.twenty.inhub.base.jwt.JwtProvider;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.controller.dto.MemberResponseDto;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.exception.MemberError;
import com.twenty.inhub.boundedContext.member.exception.MemberException;
import com.twenty.inhub.boundedContext.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/member")
@Tag(name = "회원 관련 API", description = "회원과 관련된 data 를 얻을 수 있습니다.")
@RequiredArgsConstructor
public class MemberOpenApiController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @GetMapping("/{id}")
    public MemberResponseDto findById(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("API - memberId = {}", id);

        String accessToken = request.getHeader("Authorization");

        if (!jwtProvider.verify(accessToken)) {
            throw new InvalidTokenException();
        }

        Optional<Member> member = memberService.findById(id);

        if(member.isEmpty()) {
            throw new NotFoundException("회원 ID가 유효하지 않습니다.");
        }

        return new MemberResponseDto(member.get());
    }

    @GetMapping
    public List<MemberResponseDto> findAll(HttpServletRequest request) {
        log.info("API - memberAll");

        String accessToken = request.getHeader("Authorization");

        if (!jwtProvider.verify(accessToken)) {
            throw new InvalidTokenException();
        }

        List<Member> members = memberService.findAll();

        return members.stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }
}
