package com.twenty.inhub.base.batch;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/* 나중에 예정
//스케줄러가 실행되면서 배치 실행
@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchScheduler {

    //job 실행
    private final JobLauncher jobLauncher;

    private final BatchConfig batchConfig;
    private final JobRepository jobRepository;


    //매일 오전 6시에 스케줄링
    @Scheduled(cron = "0 0 6 * * *")
    public void runJob() throws JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException {
        log.debug("배치 스케줄링 진행");
        jobLauncher.run(batchConfig.job1(jobRepository),
                new JobParametersBuilder().addDate("date", new Date()).toJobParameters());


    }
}

 */
