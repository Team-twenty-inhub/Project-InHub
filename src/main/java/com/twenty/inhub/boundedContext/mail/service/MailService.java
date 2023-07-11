package com.twenty.inhub.boundedContext.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;
    private String ePw; // 인증번호

    // 메일 내용 작성(회원가입 이메일 인증)
    private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("Mail to : {}, Authorization Key : {}", to, ePw);

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);// 보내는 대상
        message.setSubject("INHUB 회원가입 이메일 인증");// 제목

        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h1> 안녕하세요</h1>";
        msg += "<h1> 개발자 면접 대비 문제풀이 서비스 INHUB 입니다</h1>";
        msg += "<br>";
        msg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msg += "<br>";
        msg += "<p>성공적인 취업을 응원합니다. 감사합니다!<p>";
        msg += "<br>";
        msg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg += "<div style='font-size:130%'>";
        msg += "CODE : <strong>";
        msg += ePw + "</strong><div><br/> "; // 메일에 인증번호 넣기
        msg += "</div>";
        message.setText(msg, "utf-8", "html");// 내용, charset 타입, subtype
        message.setFrom(new InternetAddress("rlarudfuf802@naver.com", "INHUB"));// 보내는 사람의 이메일 주소, 보내는 사람 이름

        return message;
    }

    // 메일 내용 작성(임시 비밀번호 발급)
    private MimeMessage createTempPwMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("Mail to : {}, Temporary Password : {}", to, ePw);

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);// 보내는 대상
        message.setSubject("INHUB 임시 비밀번호 발급");// 제목

        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h1> 안녕하세요</h1>";
        msg += "<h1> 개발자 면접 대비 문제풀이 서비스 INHUB 입니다</h1>";
        msg += "<br>";
        msg += "<p>아래 임시 비밀번호를 사용해 로그인 해주세요.<p>";
        msg += "<br>";
        msg += "<h3 style='color:red; font-weight:bold;'>로그인 후, 반드시 비밀번호를 재설정 해주세요.<h3>";
        msg += "<br>";
        msg += "<p>성공적인 취업을 응원합니다. 감사합니다!<p>";
        msg += "<br>";
        msg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg += "<h3 style='color:blue;'>임시 비밀번호입니다.</h3>";
        msg += "<div style='font-size:130%'>";
        msg += "PASSWORD : <strong>";
        msg += ePw + "</strong><div><br/> ";
        msg += "</div>";
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("rlarudfuf802@naver.com", "INHUB"));

        return message;
    }

    // 메일 내용 작성(기기 인증코드 발급)
    private MimeMessage createDeviceAuthenticationMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("Mail to : {}, Device Authentication Code : {}", to, ePw);

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);// 보내는 대상
        message.setSubject("INHUB 기기 인증코드 발급");// 제목

        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h1> 안녕하세요</h1>";
        msg += "<h1> 개발자 면접 대비 문제풀이 서비스 INHUB 입니다</h1>";
        msg += "<br>";
        msg += "<p>아래 기기 인증코드를 사용해 기기 인증을 완료해주세요.<p>";
        msg += "<br>";
        msg += "<p>성공적인 취업을 응원합니다. 감사합니다!<p>";
        msg += "<br>";
        msg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg += "<h3 style='color:blue;'>기기 인증코드입니다.</h3>";
        msg += "<div style='font-size:130%'>";
        msg += "AUTHENTICATION CODE : <strong>";
        msg += ePw + "</strong><div><br/> ";
        msg += "</div>";
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("rlarudfuf802@naver.com", "INHUB"));

        return message;
    }

    // 랜덤 키 생성
    private String createKey() {
        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char) (random.nextInt(26) + 97));
                    // a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 65));
                    // A~Z
                    break;
                case 2:
                    key.append(random.nextInt(10));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    // 회원가입 이메일 인증 메일 발송
    public String sendSimpleMessage(String to) throws Exception {
        ePw = createKey(); // 랜덤 인증번호 생성

        MimeMessage message = createMessage(to); // 메세지 내용 생성
        try {
            emailSender.send(message); // 메세지 발송
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
    }

    // 임시 비밀번호 발급 메일 발송
    public String sendSimpleMessageForTempPw(String to) throws MessagingException, UnsupportedEncodingException {
        ePw = createKey();

        MimeMessage message = createTempPwMessage(to);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        return ePw;
    }

    // 기기 인증번호 발급 메일 발송
    public String sendSimpleMessageForDeviceAuthentication(String to) throws MessagingException, UnsupportedEncodingException {
        ePw = createKey();

        MimeMessage message = createDeviceAuthenticationMessage(to);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        return ePw;
    }
}