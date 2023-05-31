package com.twenty.inhub.boundedContext.Answer.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.Answer.entity.Answer;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerCheckRepository;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerQueryRepository;
import com.twenty.inhub.boundedContext.Answer.repository.AnswerRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;

    private final AnswerCheckRepository answerCheckRepository;

    private final AnswerQueryRepository answerQueryRepository;


    // 정답 달때 사용
    public void create(Question question,Member member, String content){
        Answer answer = Answer.builder()
                .content(content)
                .question(question)
                .member(member)
                .build();


        this.answerRepository.save(answer);
        question.getAnswers().add(answer);
        member.getAnswers().add(answer);

    }

    //출제자 질문 등록시 정답 등록
    public RsData<Answer> createAnswer(Question question, Member member, String word1,String word2,String word3) {
        if(member.getRole().equals("JUNIOR"))
        {
            return RsData.of("F-44","권한이 없습니다.");
        }
        Answer answer = Answer.builder()
                .word1(word1)
                .word2(word2)
                .word3(word3)
                .question(question)
                .build();

        this.answerCheckRepository.save(answer);
        return RsData.of("S-1","답변 등록 완료",answer);
    }

    //등록한 정답
    @Transactional(readOnly = true)
    public Answer findAnswer(Long id){
        Answer answer = this.answerRepository.findById(id).orElse(null);
        return answer;
    }

    //진짜 정답 찾아오기
    @Transactional(readOnly = true)
    public Answer findAnswerCheck(Question question){
        return this.answerCheckRepository.findByQuestionId(question.getId()).orElse(null);

    }

    //Check Answer => 답이 맞는지
    public RsData<Answer> checkAnswer(Question question, Member member,String content){
        Answer checkAnswer = findAnswerCheck(question);


        if(checkAnswer == null){
            return RsData.failOf(null);
        }

        int count = ScoreCount(0,checkAnswer,content);


        //그래도 1개는 맞춘 답만 올라가게
        if(count >= 1){
             create(question,member,content);
        }

        if(count == 1){
            return RsData.of("S-1541",count+"개 일치",checkAnswer);
        }

        if(count == 2){
            return RsData.of("S-1542",count+"개 일치",checkAnswer);
        }
        if(count == 3){
            return RsData.of("S-1543",count+"개 일치",checkAnswer);
        }

        return RsData.of("F-55","오답",checkAnswer);
    }

    private int ScoreCount(int Score,Answer checkAnswer, String content) {

        if(content.contains(checkAnswer.getWord1())){
            Score+=1;
        }
        if(content.contains(checkAnswer.getWord2()))
        {
            Score+=1;
        }
        if(content.contains(checkAnswer.getWord3()))
        {
            Score +=1;
        }

        return Score;
    }

    //답 수정
    public RsData<Answer> updateAnswer(Long id,Member member,String content){
        Answer answer = findAnswer(id);
        if(!Objects.equals(answer.getMember().getId(),member.getId())){
            return RsData.of("F-887","수정 권한이 없습니다.");
        }
        answer.modifyContent(content);

        return RsData.of("S-455","수정이 완료되었습니다.",answer);
    }
    public RsData<Answer> canUpdateAnswer(Member member, Answer answer) {
        if(answer == null){
            return RsData.of("F-8546", "답변이 존재하지 않습니다.");
        }
        if(member.getId() != answer.getMember().getId())
        {
            return RsData.of("F-7885","권한이 없습니다.");
        }

        return RsData.of("S-48","수정 가능");
    }


    //답 삭제
    public void deleteAnswer(Answer answer){
        answer.getQuestion().getAnswers().remove(answer);
        this.answerRepository.delete(answer);
    }


    public RsData<Answer> CanDeleteAnswer(Member member, Answer answer) {
        if(answer == null)
        {
            return RsData.of("F-887","이미 삭제한 답변입니다.");
        }

        long memberId = member.getId();
        long answerMemberId = answer.getMember().getId();
        if(memberId != answerMemberId){
            return RsData.of("F-888","권한이 없습니다.");
        }
        return RsData.of("S-887","삭제 가능");
    }


}