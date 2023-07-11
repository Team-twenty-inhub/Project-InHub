//package com.twenty.inhub.base.appConfig;
//
//import com.twenty.inhub.base.interceptor.DeviceCheckInterceptor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    private final DeviceCheckInterceptor deviceCheckInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(deviceCheckInterceptor)
//                .addPathPatterns("/", "/member/**", "/post/**", "/question/**", "/answer/**", "/search/**", "/note/**", "/rooms/**", "/book/**");
//    }
//}
