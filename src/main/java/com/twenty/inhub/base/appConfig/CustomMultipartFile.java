package com.twenty.inhub.base.appConfig;

import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;

public class CustomMultipartFile implements MultipartFile {
    private final File file;

    public CustomMultipartFile(File file) {
        this.file = file;
    }

    @Override
    public String getOriginalFilename() {
        return file.getName();
    }

    @Override
    public boolean isEmpty() {
        return file.length() == 0;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        byte[] bytes = new byte[(int) getSize()];
        try (InputStream in = new FileInputStream(file)) {
            in.read(bytes);
        }
        return bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        Files.copy(file.toPath(), dest.toPath());
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getContentType() {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            return null;
        }
    }
}

