package com.twenty.inhub.boundedContext.Answer.controller;

import com.twenty.inhub.boundedContext.Answer.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AnswerController {

    private AnswerService answerService;
}