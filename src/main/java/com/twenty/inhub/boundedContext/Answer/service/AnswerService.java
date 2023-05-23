package com.twenty.inhub.boundedContext.Answer.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerQueryRepository;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerRepository;
import com.twenty.inhub.boundedContext.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    private final AnswerQueryRepository answerQueryRepository;


    private RsData<Answer> create(Question question,String content){
        Answer answer = Answer.builder().build();


        answerRepository.save(answer);

        return RsData.of("S-1","답변 등록 완료");
    }




}
