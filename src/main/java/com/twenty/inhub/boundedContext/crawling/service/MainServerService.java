package com.twenty.inhub.boundedContext.crawling.service;

import com.twenty.inhub.boundedContext.crawling.dto.CrawledJobDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MainServerService {
    private static final int PAGE_SIZE = 18;

    public List<CrawledJobDto> processCrawledJobs(CrawledJobDto[] crawledJobs) {
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

    public List<List<CrawledJobDto>> paginateJobs(List<CrawledJobDto> crawledJobs) {
        List<List<CrawledJobDto>> paginatedJobs = new ArrayList<>();
        int totalJobs = crawledJobs.size();
        int totalPages = (int) Math.ceil((double) totalJobs / PAGE_SIZE);

        for (int i = 0; i < totalPages; i++) {
            int fromIndex = i * PAGE_SIZE;
            int toIndex = Math.min(fromIndex + PAGE_SIZE, totalJobs);

            List<CrawledJobDto> page = crawledJobs.subList(fromIndex, toIndex);
            paginatedJobs.add(page);
        }

        return paginatedJobs;
    }

    public List<CrawledJobDto> searchJobs(List<CrawledJobDto> crawledJobs, String search) {
        if (search == null || search.isEmpty()) {
            return crawledJobs;
        }

        // 검색어로 필터링된 작업 목록 생성
        List<CrawledJobDto> searchedJobs = new ArrayList<>();
        for (CrawledJobDto job : crawledJobs) {
            if (job.getCompany().contains(search) || job.getDetail().contains(search)) {
                searchedJobs.add(job);
            }
        }

        return searchedJobs;
    }
}

