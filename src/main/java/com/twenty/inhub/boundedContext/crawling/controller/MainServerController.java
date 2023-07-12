package com.twenty.inhub.boundedContext.crawling.controller;

import com.twenty.inhub.boundedContext.crawling.dto.CrawledJobDto;
import com.twenty.inhub.boundedContext.crawling.service.MainServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/crawling")
@RequiredArgsConstructor
public class MainServerController {
    private final MainServerService mainServerService;

    @GetMapping("/job-infos")
    public String createPostFromCrawling(Model model,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(required = false) String search) {

        RestTemplate restTemplate = new RestTemplate();
        CrawledJobDto[] list = restTemplate.getForObject("http://localhost:8081/crawling/job-infos", CrawledJobDto[].class);

        List<CrawledJobDto> crawledJobs = mainServerService.processCrawledJobs(list);
        List<CrawledJobDto> searchedJobs = mainServerService.searchJobs(crawledJobs, search);
        List<List<CrawledJobDto>> paginatedJobs = mainServerService.paginateJobs(searchedJobs);

        // 현재 페이지 인덱스를 모델에 추가
        model.addAttribute("currentPage", page);
        // 전체 페이지 목록을 모델에 추가
        model.addAttribute("pages", paginatedJobs);
        model.addAttribute("search", search);


        // 현재 페이지에 해당하는 카드 목록을 추출
        List<CrawledJobDto> currentPageCards = paginatedJobs.get(page);
        model.addAttribute("cards", currentPageCards);

        return "usr/post/job";
    }
}
