package com.twenty.inhub.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Getter
    private static long minSizeForSenior;


    @Getter
    private static String domain;


    @Value("${custom.site.baseUrl}")
    public void setDomain(String domain){
        AppConfig.domain = domain;
    }

    @Value("${custom.member.rating.senior.min}")
    public void setMinRatingSize(long minSizeForSenior) {
        AppConfig.minSizeForSenior = minSizeForSenior;
    }
}
