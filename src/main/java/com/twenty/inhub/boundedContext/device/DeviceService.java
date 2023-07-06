package com.twenty.inhub.boundedContext.device;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.mail.service.MailService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final MailService mailService;

    public RsData<String> createCode(Member member) {
        String code = "";
        try {
            code = mailService.sendSimpleMessageForDeviceAuthentication(member.getEmail());
        } catch (Exception e) {
            return RsData.of("F-1", "기기 인증코드 발급 오류가 발생했습니다.");
        }

        return RsData.of("S-1", "기기 인증코드가 발급 되었습니다.<br>이메일을 확인해주세요.", code);
    }

    @Transactional
    public RsData<Device> addAuthenticationDevice(Member member, String info) {
        Device device = Device.builder()
                .member(member)
                .info(info)
                .build();

        Device saved = deviceRepository.save(device);

        member.getDevices().add(saved);

        return RsData.of("S-1", "기기 인증 성공", saved);
    }
}
