package com.twenty.inhub.boundedContext.post.controller;

import com.twenty.inhub.boundedContext.post.dto.CrawledJobDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/crawling")
@RequiredArgsConstructor
public class MainServerController {
    @GetMapping("/job-infos")
    public String createPostFromCrawling(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        CrawledJobDto[] list = restTemplate.getForObject("http://localhost:8081/crawling/job-infos", CrawledJobDto[].class);

        List<CrawledJobDto> crawledJobs = processCrawledJobs(list);

        model.addAttribute("Dto", crawledJobs);

        return "usr/post/job";
    }

    private List<CrawledJobDto> processCrawledJobs(CrawledJobDto[] crawledJobs) {
        // 크롤링 서버에서 가져온 데이터를 적절하게 가공 또는 필요한 필드를 추출하는 로직 구현
        // ProcessedJobDto로 변환하여 반환
        List<CrawledJobDto> processedJobs = new ArrayList<>();
        for (CrawledJobDto crawledJob : crawledJobs) {
            CrawledJobDto processedJob = new CrawledJobDto();

            processedJob.setCompany(crawledJob.getCompany());
            processedJob.setDetail(crawledJob.getDetail());
            processedJob.setJobUrl(crawledJob.getJobUrl());
            processedJob.setExperience(crawledJob.getExperience());
            processedJob.setLocation(crawledJob.getLocation());
            processedJob.setApply(crawledJob.getApply());

            processedJobs.add(processedJob);
        }
        return processedJobs;
    }
}
