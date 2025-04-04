package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileStorageService;

@Service
public class FileStorageServiceImpl  implements FileStorageService{

    public static final  String UPLOAD_DIR = "images/";
    @Override
   public String saveImage(MultipartFile file, String fileName) throws IOException {
    Path filePath = Paths.get(UPLOAD_DIR, fileName);
    
    
    Files.createDirectories(filePath.getParent());
    
    
    Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    BufferedImage image = ImageIO.read(new File(filePath.toString()));
    if (image == null) {
            throw new IOException("Không thể đọc kích thước ảnh: " + fileName);
        }

        Integer width = image.getWidth();
        Integer height = image.getHeight();

    
    return fileName + " " + width + " " + height;
    
}
    @Override
    public Boolean deleteImage(String imageUrl) {
        Path filePath = Paths.get(UPLOAD_DIR, imageUrl);
        try {
            Files.deleteIfExists(filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
