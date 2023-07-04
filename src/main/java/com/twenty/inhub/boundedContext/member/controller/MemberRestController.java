package com.twenty.inhub.boundedContext.member.controller;

import com.twenty.inhub.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/checkDuplicate/{field}")
    public ResponseEntity<?> checkDuplicateField(@PathVariable String field, @RequestBody Map<String, String> request) {
        log.info("중복체크({}) = {}", field, request);

        // 비밀번호는 중복 체크를 안해도 된다.
        if(field.equals("password")) {
            return ResponseEntity.ok("사용 가능한 " + field + "입니다.");
        }

        String value = request.get("value");

        boolean isDuplicate = memberService.isDuplicateField(field, value);

        if (isDuplicate) {
            return ResponseEntity.ok("중복된 " + field + "입니다.");
        }

        return ResponseEntity.ok("사용 가능한 " + field + "입니다.");
    }

    @PostMapping("/checkLength/{field}")
    public ResponseEntity<?> checkLengthField(@PathVariable String field, @RequestBody Map<String, String> request) {
        log.info("길이체크({}) = {}", field, request);

        String value = request.get("value");

        boolean lengthCheck = memberService.checkLengthField(field, value);

        if (!lengthCheck) {
            return ResponseEntity.ok(field + "의 글자 수가 올바르지 않습니다.");
        }

        return ResponseEntity.ok("사용 가능한 " + field + "입니다.");
    }
}