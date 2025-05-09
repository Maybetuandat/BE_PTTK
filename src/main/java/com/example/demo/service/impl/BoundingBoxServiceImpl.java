package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.BoundingBox;
import com.example.demo.repository.BoundingBoxRepository;
import com.example.demo.service.BoundingBoxService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BoundingBoxServiceImpl implements BoundingBoxService {

    @Autowired
    BoundingBoxRepository boundingBoxRepository;
    
    @Override
    public List<BoundingBox> getBoundingBoxsWithFraudTemplateId(Integer fraudTemplateId) {
        List<BoundingBox> boundingBoxList = null;
        try {
            boundingBoxList = boundingBoxRepository.findByFraudTemplateId(fraudTemplateId);
        } catch (Exception e) {
            System.out.println("Error getting bounding boxes: " + e.getMessage());
            e.printStackTrace();
        }
        return boundingBoxList;
    }

    @Override
    public BoundingBox addBoundingBox(BoundingBox boundingBox) {
        BoundingBox saveBoundingBox = null;
        try {
            saveBoundingBox = boundingBoxRepository.save(boundingBox);
        } catch (Exception e) {
            System.out.println("Error saving bounding box: " + e.getMessage());
            e.printStackTrace();
        }
        return saveBoundingBox;
    }
    @Override
    @Transactional
    public boolean deleteBoundingBoxFromTemplate(Integer templateId, Integer boxId) {
        try {
            Optional<BoundingBox> boxOpt = boundingBoxRepository.findById(boxId);
            
            if (boxOpt.isEmpty()) {
                return false;
            }
            
            BoundingBox box = boxOpt.get();
            if (box.getFraudTemplate().getId().equals(templateId)) {
                boundingBoxRepository.delete(box);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
     public BoundingBox getBoundingBoxById(Integer id) {
        return boundingBoxRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bounding box với ID: " + id));
    }
}