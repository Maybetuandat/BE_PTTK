package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.BoundingBox;
import com.example.demo.repository.BoundingBoxRepository;
import com.example.demo.service.BoundingBoxService;

@Service
public class BoundingBoxServiceImpl  implements BoundingBoxService{



    @Autowired
    BoundingBoxRepository boundingBoxRepository;
    @Override
    public List<BoundingBox> getBoundingBoxsWithFraudTemplateId(Integer fraudTemplateId) {
        List<BoundingBox> boundingBoxList = null;
          try
          {
            boundingBoxList = boundingBoxRepository.findByFraudTemplateId(fraudTemplateId);
          }
          catch (Exception e) {
              System.out.println(e);
          }
          return boundingBoxList;
    }

    @Override
    public BoundingBox addBoundingBox(BoundingBox boundingBox) {
        BoundingBox saveBoundingBox = new BoundingBox();
        try{

             saveBoundingBox = boundingBoxRepository.save(boundingBox);
        }
        catch (Exception e) {
            System.out.println(e);
        }
       return saveBoundingBox;
    }

    @Override
    public void deleteBoundingBox(Integer boundingBoxId) {
         try
         {
            boundingBoxRepository.deleteById(boundingBoxId);
         }
         catch (Exception e) {
             System.out.println(e);
         }
         
    }

}
