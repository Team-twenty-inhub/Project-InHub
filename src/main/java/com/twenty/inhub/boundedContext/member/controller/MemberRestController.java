package com.twenty.inhub.boundedContext.member.controller;

import com.twenty.inhub.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/checkDuplicate/{field}")
    public ResponseEntity<?> checkDuplicateField(@PathVariable String field, @RequestBody Map<String, String> request) {
        String value = request.get("value");

        // 중복 확인 로직 수행
        boolean isDuplicate = memberService.isDuplicateField(field, value);

        Map<String, String> response = new HashMap<>();
        if (isDuplicate) {
            response.put("message", "중복입니다.");
        } else {
            response.put("message", "사용 가능한 " + field + "입니다.");
        }

        return ResponseEntity.ok(response);
    }
}
