package com.twenty.inhub.boundedContext.Answer.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerQueryRepository;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
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


    // Create Answer
    public RsData<Answer> create(Question question, String content){
        Answer answer = Answer.builder()
                .content(content)
                .question(question)
                .build();


        this.answerRepository.save(answer);
        question.getAnswers().add(answer);

        return RsData.of("S-1","답변 등록 완료",answer);
    }

    public RsData<Answer> createAnswer(Question question, Member member, String content) {
        if(member.getRole().equals("JUNIOR"))
        {
            return RsData.of("F-44","권한이 없습니다.");
        }
        Answer answer = Answer.builder()
                .content(content)
                .question(question)
                .build();

        this.answerRepository.save(answer);
        return RsData.of("S-1","답변 등록 완료",answer);
    }

    //Find Answer
    @Transactional(readOnly = true)
    public Answer findAnswer(Long id){
        Answer answer = this.answerRepository.findById(id).orElse(null);
        return answer;
    }

    //Check Answer => 답이 맞는지
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

    //답 수정
    public Answer updateAnswer(Answer answer, String content){
        answer.modifyContent(content);

        return answer;
    }


    //답 삭제
    public void deleteAnswer(Answer answer){
        answer.getQuestion().getAnswers().remove(answer);
        this.answerRepository.delete(answer);
    }




}
