package com.twenty.inhub.base.security;

import com.twenty.inhub.boundedContext.device.Device;
import com.twenty.inhub.boundedContext.device.DeviceRepository;
import com.twenty.inhub.boundedContext.device.DeviceService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository repository;
    private final DeviceRepository deviceRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "username(%s) not found".formatted(username)
                ));

        List<String> userAgents = deviceRepository.findByMemberUsername(member.getUsername())
                .stream()
                .map(Device::getInfo)
                .toList();

        return new CustomOAuth2User(member.getId(), member.getUsername(), member.getPassword(), member.getGrantedAuthorities(), userAgents, false);
    }
}