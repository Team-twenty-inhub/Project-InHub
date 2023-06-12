package com.twenty.inhub.boundedContext.member.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.twenty.inhub.base.appConfig.S3Config;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.controller.form.MemberUpdateForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.member.entity.MemberStatus;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;
    @Value("${cloud.aws.s3.storage}")
    private String storage;

    // 일반 회원가입(임시)
    @Transactional
    public RsData<Member> create(String username, String password) {
        return create("INHUB", username, password, null);
    }

    // 내부 처리함수, 일반회원가입, 소셜로그인을 통한 회원가입(최초 로그인 시 한번만 발생)에서 이 함수를 사용함
    private RsData<Member> create(String providerTypeCode, String username, String password, String profileImg) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        // 소셜 로그인을 통한 회원가입에서는 비번이 없다.
        if (StringUtils.hasText(password)) {
            password = passwordEncoder.encode(password);
        }

        MemberRole role = MemberRole.JUNIOR;

        if(username.equals("admin")) {
            role = MemberRole.ADMIN;
        }

        Member member = Member
                .builder()
                .providerTypeCode(providerTypeCode)
                .role(role)
                .status(MemberStatus.ING)
                .username(username)
                .password(password)
                .profileImg(profileImg)
                .build();

        memberRepository.save(member);

        return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
    }

    // 소셜 로그인(카카오, 구글, 네이버) 로그인이 될 때 마다 실행되는 함수
    @Transactional
    public RsData<Member> whenSocialLogin(String providerTypeCode, String username, String profileImg) {
        Optional<Member> opMember = findByUsername(username); // username 예시 : KAKAO__1312319038130912, NAVER__1230812300

        if (opMember.isPresent()) {
            return RsData.of("S-2", "로그인 되었습니다.", opMember.get());
        }

        // 소셜 로그인를 통한 가입시 비번은 없다.
        return create(providerTypeCode, username, "", profileImg); // 최초 로그인 시 딱 한번 실행
    }

    @Transactional
    public RsData<Member> updateProfile(Member member, MemberUpdateForm form, MultipartFile mFile) {
        Optional<Member> byNickname = memberRepository.findByNickname(form.getNickname());

        if(byNickname.isPresent()) {
            if(!Objects.equals(member.getId(), byNickname.get().getId())) {
                return RsData.of("F-1", "이미 사용중인 닉네임입니다.");
            }
        }

        String profileUrl = saveImgFile(member, mFile); // 프로필 이미지 파일 저장

        member.setNickname(form.getNickname());

        if(!mFile.isEmpty()) {
            member.setProfileImg(profileUrl);
        }

        memberRepository.save(member);

        return RsData.of("S-1", "프로필 수정 완료");
    }

    private String saveImgFile(Member member, MultipartFile mFile) {
        if(mFile.isEmpty()) {
            return "";
        }

        String fileName = "profileImage_userId_" + member.getId();
        String profileUrl = "https://s3." + s3Config.getRegion() + ".amazonaws.com/" + s3Config.getBucket() + "/" + storage + "/" + fileName;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(mFile.getContentType());
            metadata.setContentLength(mFile.getSize());

            amazonS3.putObject(new PutObjectRequest(s3Config.getBucket(), storage + "/" + fileName, mFile.getInputStream(), metadata));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return profileUrl;
    }

    @Transactional
    public RsData<Member> updateRole(Member member, String role) {
        switch (role) {
            case "ADMIN" -> member.setRole(MemberRole.ADMIN);
            case "JUNIOR" -> member.setRole(MemberRole.JUNIOR);
            case "SENIOR" -> member.setRole(MemberRole.SENIOR);
        }

        return RsData.of("S-5", "%s의 역할이 %s(으)로 변경되었습니다.".formatted(member.getUsername(), role));
    }

    @Transactional
    public RsData<Member> updateStatus(Member member, String status) {
        switch (status) {
            case "ING" -> member.setStatus(MemberStatus.ING);
            case "STOP" -> member.setStatus(MemberStatus.STOP);
        }

        return RsData.of("S-6", "%s의 상태가 %s(으)로 변경되었습니다.".formatted(member.getUsername(), status));
    }

    public Page<Member> getMemberList(int page, String kw, String searchBy) {
        Pageable pageable = PageRequest.of(page, 5);

        Specification<Member> spec = search(kw, searchBy);

        return memberRepository.findAll(spec, pageable);
    }

    private Specification<Member> search(String kw, String searchBy) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Member> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                String keyword = "%" + kw.toLowerCase() + "%"; // 입력된 키워드를 소문자로 변환하여 사용

                return switch (searchBy) {
                    case "nickname" -> cb.like(cb.lower(q.get("nickname")), keyword);
                    case "role" -> cb.like(cb.lower(q.get("role")), keyword);
                    case "status" -> cb.like(cb.lower(q.get("status")), keyword);
                    default -> cb.like(cb.lower(q.get("username")), keyword);
                };
            }
        };
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    // 임시 : 포인트를 올려주는 메소드
    @Transactional
    public void increasePoint(Member member) {
        member.setPoint(member.getPoint()+100);
    }

    // 임시 : 포인트를 내려주는 메소드
    @Transactional
    public void decreasePoint(Member member) {
        member.setPoint(member.getPoint()-100);
    }
}
