package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnderlineService {

    private final UnderlineQueryRepository underlineQueryRepository;
    private final UnderlineRepository underlineRepository;


    //-- create --//
    @Transactional
    public RsData<Underline> create(String about, Member member, Question question) {
        RsData<Underline> byMemberQuestion = this.findByMemberQuestion(member, question);

        if (byMemberQuestion.isSuccess())
            return RsData.of("F-1", "이미 밑줄친 문제입니다.");

        if (byMemberQuestion.getResultCode().equals("F-2"))
            return byMemberQuestion;

        Underline underline = underlineRepository.save(
                Underline.createUnderline(about, member, question)
        );

        return RsData.of("S-1", question.getName() + " 문제 밑줄 긋기 완료");
    }


    //-- find by about --//
    public RsData<Underline> findByAbout(String about) {
        Optional<Underline> byAbout = underlineRepository.findByAbout(about);

        if (byAbout.isPresent())
            return RsData.successOf(byAbout.get());

        return RsData.of("F-1", "존재하지 않는 내용입니다.");
    }

    //-- find by member id , question id --//
    public RsData<Underline> findByMemberQuestion(Member member, Question question) {
        List<Underline> underlines = underlineQueryRepository.findByMemberQuestion(member.getId(), question.getId());

        if (underlines.size() == 1)
            return RsData.successOf(underlines.get(0));

        else if (underlines.size() == 0)
            return RsData.of("F-1", "저장된 밑줄이 없습니다.");

        return RsData.of("F-2", "밑줄이 2개 이상입니다.");
    }
}
