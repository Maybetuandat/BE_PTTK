package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import com.example.demo.service.FileStorageService;




@Service
public class FileStorageServiceImpl  implements FileStorageService{

    public static final  String UPLOAD_DIR = "images/";
    public static final String BASE_URL = "http://localhost:8080/images/";
    @Override
    public List<String> saveImage(MultipartFile[] files) throws IOException {

        List<String> listSaveFileName = new ArrayList<>();
        for(MultipartFile file : files)
        {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            BufferedImage image = ImageIO.read(filePath.toFile());
            int width = image.getWidth();
            int height = image.getHeight();


            listSaveFileName.add(BASE_URL + fileName + " " + width + " " + height);
            System.out.println("File saved: " + fileName);
            System.out.println("Width: " + width);
            System.out.println("Height: " + height);
        }
        return listSaveFileName;
          
    }
    @Override
    public Boolean deleteImage(String imageUrl) throws IOException {
         
        String filePath = imageUrl.replace(BASE_URL, UPLOAD_DIR);
        System.out.println(imageUrl);
        System.out.println(filePath);
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
             return true;
            } else {
               return false;
            }
        } 
        return false;
        
    }



}
