package com.twenty.inhub.boundedContext.answer.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.answer.controller.AnswerController.AnswerCheckForm;
import com.twenty.inhub.boundedContext.answer.controller.dto.AnswerDto;
import com.twenty.inhub.boundedContext.answer.entity.Answer;
import com.twenty.inhub.boundedContext.answer.entity.AnswerCheck;
import com.twenty.inhub.boundedContext.answer.entity.Keyword;
import com.twenty.inhub.boundedContext.answer.event.AnswerCheckPointEvent;
import com.twenty.inhub.boundedContext.answer.repository.AnswerCheckRepository;
import com.twenty.inhub.boundedContext.answer.repository.AnswerQueryRepository;
import com.twenty.inhub.boundedContext.answer.repository.AnswerRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;

    private final AnswerCheckRepository answerCheckRepository;

    private final AnswerQueryRepository answerQueryRepository;

    private final ApplicationEventPublisher publisher;


    // 정답 달때 사용
    public Answer create(Question question, Member member, String content, String result,int score) {
        Answer answer = Answer.builder()
                .content(content)
                .question(question)
                .member(member)
                .result(result)
                .score(score)
                .build();

        return answer;
    }

    public void AddAnswer(Answer answer, Member member, Question question) {
        Answer answer1 = findAnswer(member.getId(), question.getId());

        //답을 저장했던 적이 없는경우 바로 저장
        if (answer1 == null) {
            this.answerRepository.save(answer);
            question.getAnswers().add(answer);
            member.getAnswers().add(answer);
        }

        //저장한경우 답 수정
        else {
            answer1.modifyContent(answer.getContent());
            answer1.modifyResult(answer.getResult());
            answer1.modifyFeedback(answer.getFeedback());
            answer1.updateScore(answer.getScore());

            answer1.getVoter().clear();
        }

        if(answer.getResult().equals("정답")){
            publisher.publishEvent(new AnswerCheckPointEvent(this,member,10));
        }

    }

    public Answer createQuizAnswer(Question question, Member member, String content) {
        Answer answer = Answer.builder()
                .content(content)
                .question(question)
                .member(member)
                .build();

        return answer;
    }

    //출제자 질문 등록시 정답 등록 :서술형
    public RsData<AnswerCheck> createAnswer(Question question, Member member, AnswerCheckForm form) {
        if (member.getRole().equals("JUNIOR")) {
            return RsData.of("F-1252", "권한이 없습니다.");
        }

        List<Keyword> keywords = createKeywords(form.getKeywords());

        AnswerCheck answer = AnswerCheck.builder()
                .member(member)
                .question(question)
                .build();

        for(Keyword keyword : keywords){
            answer.addKeyword(keyword);
        }

        this.answerCheckRepository.save(answer);
        //Question에 AnswerCheck넣을거 추가 해야함.
        question.addAnswerCheck(answer);

        return RsData.of("S-251", "답변 등록 완료", answer);
    }

    private List<Keyword> createKeywords(List<String> keywords) {
        List<Keyword> keywordList = new ArrayList<>();
        for(String keyword : keywords){
            keywordList.add(Keyword.createKeyword(keyword));
        }
        return keywordList;
    }

    //출제자 질문 등록시 정답 등록 : 객관식
    public RsData<AnswerCheck> createAnswer(Question question, Member member, String content) {
        if (member.getRole().equals("JUNIOR")) {
            return RsData.of("F-1252", "권한이 없습니다.");
        }
        AnswerCheck answer = AnswerCheck.builder()
                .content(content)
                .member(member)
                .question(question)
                .build();

        this.answerCheckRepository.save(answer);
        //Question에 AnswerCheck넣을거 추가 해야함.
        question.addAnswerCheck(answer);
        return RsData.of("S-251", "답변 등록 완료", answer);
    }

    //등록한 정답
    @Transactional(readOnly = true)
    public Answer findAnswer(Long memberId, Long questionId) {
        Answer answer = this.answerRepository.findByMemberIdAndQuestionId(memberId, questionId).orElse(null);
        return answer;
    }

    //등록한 정답
    @Transactional(readOnly = true)
    public Answer findByMemberIdAndId(Long memberId, Long answerId) {
        Answer answer = this.answerRepository.findByMemberIdAndId(memberId, answerId).orElse(null);
        return answer;
    }


    //진짜 정답 찾아오기
    @Transactional(readOnly = true)
    public AnswerCheck findAnswerCheck(Question question) {
        return this.answerCheckRepository.findByQuestionId(question.getId()).orElse(null);

    }

    //Check Answer => 답이 맞는지
    public RsData<Answer> checkAnswer(Question question, Member member, String content) {
        AnswerCheck checkAnswer = findAnswerCheck(question);
        Answer answer = findAnswer(member.getId(), question.getId());

        if (checkAnswer == null) {
            return RsData.failOf(null);
        }

        //답을 이미 적었을 경우

        if (answer != null) {
            answer.modifyContent(content);
            if (question.getType().equals(QuestionType.SAQ)) {
                int score = ScoreCount(0, checkAnswer, content);

                //70점이상인경우
                if (score >= 70) {
                    answer.modifyResult("정답");
                } else {
                    answer.modifyResult("오답");
                }


                if (score < 70) {
                    return RsData.of("F-1254", score + "점", answer);
                } else {
                    return RsData.of("S-495", "정답", answer);
                }

            }
            //객관식 채점시
            else {
                if (answer.getContent().equals(checkAnswer.getContent())) {
                    answer.modifyResult("정답");
                    answer.updateScore(100);
                    return RsData.of("S-257", "정답", answer);
                }
                answer.modifyResult("오답");
            }

        } else {
            //답을 적은 적이 없는 경우 생성
            //주관식 채점시
            if (question.getType().equals(QuestionType.SAQ)) {
                int score = ScoreCount(0, checkAnswer, content);


                //그래도 1개는 맞춘 답만 올라가게
                if (score >= 70) {
                    answer = create(question, member, content, "정답",score);
                } else {
                    answer = create(question, member, content, "오답",score);
                }

                if (score < 70) {
                    return RsData.of("F-1254", score + "점", answer);
                } else {
                    return RsData.of("S-495", "정답", answer);
                }

            }
            //객관식 채점시
            else {
                if (content.equals(checkAnswer.getContent())) {
                    answer = create(question, member, content, "정답",100);
                    return RsData.of("S-257", "정답", answer);
                }
                answer = create(question, member, content, "오답",0);
            }
        }


        return RsData.of("F-1257", "오답", answer);
    }

    private int ScoreCount(int Score, AnswerCheck checkAnswer, String content) {

        int keywordSize = checkAnswer.getKeywords().size();
        int part = 100/keywordSize;

        for(Keyword keyword : checkAnswer.getKeywords()){
            if(content.contains(keyword.getKeyword())){
                Score+= part;
            }
        }

        return Score;
    }

    //점수 합쳐서 수정 및 answer에 feedback 추가
    //점수 변경에 따른 result 변경 나중에 서비스에서 처리예정
    //확인 예정으로 40점이상으로 변경해서 진행
    public void updateAnswer(Answer answer,int modifyScore,String feedback) {
        answer.updateScore(modifyScore);
        if(answer.getScore() >= 70){
            answer.modifyResult("정답");
        }else{
            answer.modifyResult("오답");
        }

        if(answer.getFeedback() != null){
            answer.modifyFeedback(feedback);
        }else{
            answer.addFeedback(feedback);
        }

    }

    public RsData<Answer> canUpdateAnswer(Member member, Answer answer) {
        if (answer == null) {
            return RsData.of("F-1259", "답변이 존재하지 않습니다.");
        }
        if (member.getId() != answer.getMember().getId()) {
            return RsData.of("F-1260", "권한이 없습니다.");
        }

        return RsData.of("S-258", "수정 가능");
    }


    //답 삭제
    public void deleteAnswer(Answer answer) {
        answer.getQuestion().getAnswers().remove(answer);
        answer.getMember().getAnswers().remove(answer);
        this.answerRepository.delete(answer);
    }


    public RsData<Answer> CanDeleteAnswer(Member member, Answer answer) {
        if (answer == null) {
            return RsData.of("F-1261", "권한이 없습니다.");
        }
        return RsData.of("S-259", "삭제 가능");
    }

    public List<Answer> findByCorrectAnswer(Long memberId, String result) {
        List<Answer> answers = answerRepository.findByMemberIdAndResult(memberId, result);
        return answers;
    }

    public List<AnswerDto> convertToDto(List<Question> questions, List<Answer> answerList) {
        List<AnswerDto> answerDtoList = new ArrayList<>();
        for (int idx = 0; idx < questions.size(); idx++) {
            Question question = questions.get(idx);
            Answer answer = answerList.get(idx);
            AnswerDto answerDto = new AnswerDto();
            answerDto.setName(question.getName());
            answerDto.setContent(question.getContent());
            answerDto.setCategoryName(question.getCategory().getName());
            answerDto.setType(question.getType());
            answerDto.setResult(answer.getResult());
            answerDtoList.add(answerDto);
        }

        return answerDtoList;
    }

    public void vote(Answer answer, Member member) {
        answer.getVoter().add(member);
        this.answerRepository.save(answer);
    }

    public void removeVoter(Answer answer, Member member) {
        answer.getVoter().remove(member);
        this.answerRepository.save(answer);
    }

    @Transactional(readOnly = true)
    public Answer getAnswer(Long id) {
        return answerRepository.findById(id).orElse(null);
    }
}