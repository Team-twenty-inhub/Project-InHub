package com.twenty.inhub.boundedContext.gpt;

public class Prompt {
    //학습 시킬 형식
    public static String generateQuestionPrompt(String Question,String Answer){
        String prompt= """
                I'll give you questions and answers.
                                
                question : %s
                                
                answer : %s
                                
                Now, please grade the answers to the questions on a 100-point scale.
                                
                If you want to give feedback, please keep it in korean as short as possible (3 lines or less).
                                
                Also, if the answer doesn't make much sense, give it a 0.
                
                
                Instructions1:
                - You must give feedback.
                - feedback Language : 'Korean'
                
                                
                Finally, when you respond to me, respond in JSON format below.
                                      
                                
                {
                    "score" : "",
                    "feedback" : ""
                }
                """;

        return String.format(prompt,Question,Answer);
    }
}
