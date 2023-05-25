package com.twenty.inhub.boundedContext.Answer.controller;

import com.twenty.inhub.boundedContext.Answer.service.AnswerService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {


    /**
     *  AnswerForm
     *  content
     */
    //정답 적기용 Form
    @AllArgsConstructor
    @Getter
    public static class AnswerForm{

        @NotBlank
        private final String content;
    }
    private AnswerService answerService;
}
