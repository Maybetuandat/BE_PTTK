package com.example.demo.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileStorageService {
    public String saveImage(MultipartFile file, String fileName) throws IOException;
    public Boolean deleteImage(String imageUrl);
}
