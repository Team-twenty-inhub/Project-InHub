package com.twenty.inhub.boundedContext.Answer.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerQueryRepository;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerRepository;
import com.twenty.inhub.boundedContext.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;

    private final AnswerQueryRepository answerQueryRepository;


    //답변 생성
    public RsData<Answer> create(String content){
        Answer answer = Answer.builder()
                .content(content)
                .build();


        this.answerRepository.save(answer);

        return RsData.of("S-1","답변 등록 완료",answer);
    }

    //답변 찾기
    @Transactional(readOnly = true)
    public Answer findAnswer(Long id){
        Answer answer = this.answerRepository.findById(id).orElse(null);
        return answer;
    }

    //정답 체크
    @Transactional(readOnly = true)
    public RsData<Answer> checkAnswer(Long id, String content){
        Answer answer = findAnswer(id);

        if(answer == null){
            return RsData.failOf(null);
        }

        if(!answer.getContent().equals(content)){
            return RsData.of("F-34","오답",answer);
        }

        return RsData.of("S-55","정답",answer);
    }

    public Answer updateAnswer(Answer answer, String content){
        answer.setContent(content);

        return answer;
    }


    //답 삭제
    public void deleteAnswer(Answer answer){
        this.answerRepository.delete(answer);
    }




}
