package com.example.demo.dto.mapper;

import java.time.format.DateTimeFormatter;

import com.example.demo.dto.FraudTemplateDTO;
import com.example.demo.model.FraudTemplate;

public class FraudTemplateMapper {

    private static final String IMAGE_BASE_URL = "http://localhost:8080/images/";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static FraudTemplateDTO mapToDTO(FraudTemplate fraudTemplate)
    {
        FraudTemplateDTO dto = new FraudTemplateDTO();
        dto.setId(fraudTemplate.getId());
        
        dto.setName(fraudTemplate.getName());
        // Gán URL đầy đủ cho imageUrl
        if (fraudTemplate.getImageUrl() != null && !fraudTemplate.getImageUrl().isEmpty()) {
            dto.setImageUrl(IMAGE_BASE_URL + fraudTemplate.getImageUrl());
        } else {
            dto.setImageUrl(null); // Trường hợp không có ảnh
        }
        if (fraudTemplate.getCreateAt() != null) {
            dto.setCreateAt(fraudTemplate.getCreateAt().format(DATE_TIME_FORMATTER));
        } else {
            dto.setCreateAt(null);
        }
        dto.setHeight(fraudTemplate.getHeight());
        dto.setWidth(fraudTemplate.getWidth());
        
        return dto;
        
    }
    public static FraudTemplate mapToModel(FraudTemplateDTO fraudTemplateDTO)
    {
        FraudTemplate fraudTemplate = new FraudTemplate();
        fraudTemplate.setId(fraudTemplateDTO.getId());
        
        fraudTemplate.setImageUrl(fraudTemplateDTO.getImageUrl());
        fraudTemplate.setName(fraudTemplateDTO.getName());
        return fraudTemplate;
    }
}
