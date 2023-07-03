package com.twenty.inhub.base.batch;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;


    @Bean
    @JobScope
    public Job job1(JobRepository jobRepository,Step step1){
        return  new JobBuilder("job1",jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    @StepScope
    public Step step1(JobRepository jobRepository
    ,Tasklet tasklet1,PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step1",jobRepository)
                .tasklet(tasklet1,platformTransactionManager)
                .build();

    }

    @StepScope
    @Bean
    public Tasklet tasklet1(){
        return (contribution, chunkContext) -> {
            System.out.println("테스크 렛 1");
            return RepeatStatus.FINISHED;
        };
    }
}
