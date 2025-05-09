package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.BoundingBox;

@Service
public interface BoundingBoxService {

    List<BoundingBox> getBoundingBoxsWithFraudTemplateId(Integer fraudTemplateId);
    BoundingBox addBoundingBox(BoundingBox boundingBox);
    
    public boolean deleteBoundingBoxFromTemplate(Integer templateId, Integer boxId);
    public BoundingBox getBoundingBoxById(Integer id) ;
}
