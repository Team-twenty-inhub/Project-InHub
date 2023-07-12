package com.twenty.inhub.boundedContext.download.controller;


import com.twenty.inhub.boundedContext.download.service.DownloadService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class DownloadController {
    private final DownloadService downloadService;

    @GetMapping("/downloadFile/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileName) {
        return downloadService.download(fileName);
    }
}