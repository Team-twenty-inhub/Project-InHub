package com.twenty.inhub.boundedContext.gpt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.service.OpenAiService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Timed(value = "gpt.service")
public class GptService {
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;
}
