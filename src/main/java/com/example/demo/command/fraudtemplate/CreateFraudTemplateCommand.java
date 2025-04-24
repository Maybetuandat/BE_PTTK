package com.example.demo.command.fraudtemplate;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.command.Command;
import com.example.demo.model.FraudTemplate;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.FraudTemplateService;

public class CreateFraudTemplateCommand implements Command {
    private FraudTemplateService fraudTemplateService;
    private FileStorageService fileStorageService;
    private MultipartFile[] files;
    private List<FraudTemplate> createdTemplates;
    private List<String> savedFileNames;

    public CreateFraudTemplateCommand(FraudTemplateService fraudTemplateService, 
                                    FileStorageService fileStorageService, 
                                    MultipartFile[] files) {
        this.fraudTemplateService = fraudTemplateService;
        this.fileStorageService = fileStorageService;
        this.files = files;
    }

    @Override
    public void execute() throws RuntimeException {
        try {
            savedFileNames = fileStorageService.saveImage(files);
            
            for (String fileName : savedFileNames) {
                FraudTemplate fraudTemplate = new FraudTemplate();
                String[] splitToGetHeightAndWidth = fileName.split("\\s+");
                String imageUrl = splitToGetHeightAndWidth[0];
                fraudTemplate.setImageUrl(imageUrl);
                String width = splitToGetHeightAndWidth[1];
                String height = splitToGetHeightAndWidth[2];
                fraudTemplate.setHeight(Integer.parseInt(height));
                fraudTemplate.setWidth(Integer.parseInt(width));

                String fileNameWithoutUrl = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                fraudTemplate.setName(fileNameWithoutUrl);

                FraudTemplate savedTemplate = fraudTemplateService.addFraudTemplate(fraudTemplate);
                if (createdTemplates != null) {
                    createdTemplates.add(savedTemplate);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo template: " + e.getMessage(), e);
        }
    }

    @Override
    public void undo() {
        if (createdTemplates != null && !createdTemplates.isEmpty()) {
            for (FraudTemplate template : createdTemplates) {
                fraudTemplateService.deleteFraudTemplate(template.getId());
            }
        }
        // Xóa các file đã lưu nếu cần
    }
    
    public List<FraudTemplate> getCreatedTemplates() {
        return createdTemplates;
    }
}