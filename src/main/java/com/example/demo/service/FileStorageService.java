package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileStorageService {
   public Boolean deleteImage(String imageUrl) throws IOException;
   public List<String> saveImage(MultipartFile[] files) throws IOException;
}
