package com.twenty.inhub.boundedContext.download.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.twenty.inhub.base.appConfig.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DownloadService {
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;
    @Value("posts")
    private String storage;

    public ResponseEntity<Resource> download(String fileName) {
        String fileUrl = "https://s3." + s3Config.getRegion() + ".amazonaws.com/" + s3Config.getBucket() + "/" + storage + "/" + fileName;

        // S3에 파일이 저장되어 있는지 확인하는 로직
        S3Object s3Object = amazonS3.getObject(s3Config.getBucket(), storage + "/" + fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        // 파일 데이터를 읽어옴
        byte[] fileData;
        try {
            fileData = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            // 파일 데이터를 읽어오는데 실패한 경우에 대한 예외 처리를 진행해야 합니다.
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        // 파일 데이터로 Resource 생성
        Resource resource = new ByteArrayResource(fileData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}

