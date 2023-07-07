package com.twenty.inhub.boundedContext.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import com.twenty.inhub.base.appConfig.GptConfig;
import com.twenty.inhub.boundedContext.answer.controller.dto.QuestionAnswerDto;
import com.twenty.inhub.boundedContext.gpt.dto.GptResponseDto;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
@Timed(value = "gpt.service")
public class GptService {

    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    public CompletableFuture<ChatCompletionResult> generated(List<ChatMessage> chatMessages) {

        ChatCompletionRequest build = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .maxTokens(GptConfig.MAX_TOKEN)
                .temperature(GptConfig.TEMPERATURE)
                .topP(GptConfig.TOP_P)
                .model(GptConfig.MODEL)
                .build();

        return CompletableFuture.supplyAsync(() -> openAiService.createChatCompletion(build));
    }

    public CompletableFuture<List<ChatMessage>> generatedQuestionAndAnswerMessage(QuestionAnswerDto questionAnswerDto) {
        String prompt = Prompt.generateQuestionPrompt(questionAnswerDto.getContent(), questionAnswerDto.getAnswer());

        log.info("생성된 프롬프트 : {}", prompt);

        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);

        return CompletableFuture.completedFuture(List.of(userMessage));
    }

    @Async
    public CompletableFuture<GptResponseDto> askQuestion(QuestionAnswerDto questionAnswerDto) {
        return generatedQuestionAndAnswerMessage(questionAnswerDto)
                .thenCompose(this::generated)
                .thenApply(result -> {
                    String gptAnswer = result.getChoices().get(0).getMessage().getContent();
                    log.info("GPT 답변: {}", gptAnswer);
                    // JSON 문자열을 파싱하여 결과 값을 추출
                    try {
                        JsonNode jsonNode = objectMapper.readTree(gptAnswer);
                        String score = jsonNode.get("score").asText();
                        String feedback = jsonNode.get("feedback").asText();

                        log.info("점수 : {}", score);
                        log.info("피드백 : {}", feedback);

                        // 결과를 담을 GptResponseDto 객체 생성
                        GptResponseDto response = new GptResponseDto();
                        response.setScore(Double.parseDouble(score));
                        response.setFeedBack(feedback);

                        return response;
                    } catch (JsonProcessingException e) {
                        log.error("Error parsing GPT response JSON: {}", e.getMessage());
                        return null;
                    }



                })
                .exceptionally(e->{
                    log.error("Error ocurred duing GPT processing:{}",e.getMessage());

                    return null;
                });
    }

}
