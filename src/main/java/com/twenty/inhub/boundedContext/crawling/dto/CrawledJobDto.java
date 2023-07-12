package com.twenty.inhub.boundedContext.crawling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CrawledJobDto {
    private String company;
    private String detail;
    private String jobUrl;
    private String experience;
    private String location;
    private String apply;
}

