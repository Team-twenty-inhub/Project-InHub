package com.twenty.inhub.base.appConfig;


import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class GptConfig {

    //쓰는 모델
    public final static String MODEL = "gpt-3.5-turbo";
    
    //생성된 텍스트의 다양성 -> 0 ~ 1.0 실수로 설정
    //작은 값으로 설정할수록 다양한 텍스트 생성
    public final static double TOP_P = 1.0;
    
    //최대 토큰 수 -> 현재 최대 2000토큰으로 조절
    public final static int MAX_TOKEN = 2000;
    //샘플링 온도 조절
    public final static double TEMPERATURE = 1.0;

    //타임아웃 기간 지정
    public final static Duration TIME_OUT = Duration.ofSeconds(300);

    @Value("${chatgpt.apikey}")
    private String token;

    @Bean
    public OpenAiService openAiService(){
        return new OpenAiService(token,TIME_OUT);
    }
}
