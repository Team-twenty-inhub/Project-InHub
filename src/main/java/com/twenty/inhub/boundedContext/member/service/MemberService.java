package com.twenty.inhub.boundedContext.member.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.controller.MemberController;
import com.twenty.inhub.boundedContext.member.controller.form.MemberUpdateForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.member.entity.MemberStatus;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

        saveImgFile(member, mFile); // 프로필 이미지 파일 저장

        member.setNickname(form.getNickname());

        if(!mFile.isEmpty()) {
            member.setProfileImg(mFile.getOriginalFilename());
        }

        memberRepository.save(member);

        return RsData.of("S-1", "프로필 수정 완료");
    }

    private void saveImgFile(Member member, MultipartFile mFile) {
        if(mFile.isEmpty()) {
            return;
        }

        /*
        File 객체는 절대 경로만 사용 가능한데,
        우리는 협업중이므로 각자의 컴퓨터마다 저장되는 공간이 다르기에 상대경로가 필요하다.
        하지만 상대경로를 구해도 File 객체에 적용이 불가능하니
        결국 각자 컴퓨터마다 static 디렉토리의 절대경로가 필요하다.
        현재 MemberController 클래스의 저장된 절대경로를 구하고
        "out/" 문자열을 기준으로 자르면 앞에 있는 문자열이 해당 컴퓨터의 절대경로 일부분이 나온다.
        그 경로 뒤에 원하는 저장공간의 절대주소를 붙여주면, 해당 컴퓨터에 저장할 공간인 최종 절대경로를 구할 수 있다.
         */
        String path = MemberController.class.getResource("").getPath();
        String[] pathBits = path.split("out/");
        String uploadPath = pathBits[0] + "src/main/resources/static/images/profile/"; // 프로필 사진들 모아두는 폴더

        try {
            if (member.getProfileImg() != null) { // 이미 프로필 사진이 있을경우
                File file = new File(uploadPath + member.getProfileImg()); // 경로 + 유저 프로필사진 이름을 가져와서
                file.delete(); // 원래파일 삭제
            }
            mFile.transferTo(new File(uploadPath + mFile.getOriginalFilename()));  // 경로에 업로드
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public RsData<Member> updateRole(Member member, String role) {
        switch (role) {
            case "ADMIN" -> member.setRole(MemberRole.ADMIN);
            case "JUNIOR" -> member.setRole(MemberRole.JUNIOR);
            case "SENIOR" -> member.setRole(MemberRole.SENIOR);
        }

        return RsData.of("S-5", "역할이 %s(으)로 변경되었습니다.".formatted(role));
    }

    @Transactional
    public RsData<Member> updateStatus(Member member, String status) {
        switch (status) {
            case "ING" -> member.setStatus(MemberStatus.ING);
            case "STOP" -> member.setStatus(MemberStatus.STOP);
        }

        return RsData.of("S-6", "상태가 %s(으)로 변경되었습니다.".formatted(status));
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
}
