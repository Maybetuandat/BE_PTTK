package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.BoundingBox;
import com.example.demo.model.FraudLabel;
import com.example.demo.model.FraudTemplate;
import com.example.demo.service.BoundingBoxService;
import com.example.demo.service.FraudLabelService;
import com.example.demo.service.FraudTemplateService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/bounding-box")
public class BoundingBoxController {
    @Autowired
    BoundingBoxService boundingBoxService;

    @Autowired 
    FraudLabelService fraudLabelService;
    @Autowired
    FraudTemplateService fraudTemplateService;


    @GetMapping("")
    public ResponseEntity<List<BoundingBox>> getBoundingBoxWithTemplateId(@RequestParam Integer fraudTemplateId) {

        return  ResponseEntity.ok(boundingBoxService.getBoundingBoxsWithFraudTemplateId(fraudTemplateId));
       
    }
    @PostMapping("")
    public ResponseEntity<BoundingBox> createBoundingBox(@ModelAttribute BoundingBox boundingBox,
                                                        @RequestParam (required = false) Integer fraudLabelId,
                                                        @RequestParam (required = false) Integer fraudTemplateId) {
        //TODO: process POST request
        
      if(fraudLabelId != null) {
            FraudLabel fraudLabel = fraudLabelService.getFraudLabelById(fraudLabelId);
            if(fraudLabel != null)
            {
                boundingBox.setFraudLabel(fraudLabel);
            }
            else{
                System.out.println("FraudLabel not found");
                return ResponseEntity.badRequest().build();
            }
           
        }
    if(fraudTemplateId != null) {
            FraudTemplate fraudTemplate = fraudTemplateService.getFraudTemplateById(fraudTemplateId);
            if(fraudTemplate != null)
            {
                boundingBox.setFraudTemplate(fraudTemplate);
            }
            else{
                System.out.println("FraudTemplate not found");
                return ResponseEntity.badRequest().build();
            }
        
        }

        try
        {
           return ResponseEntity.ok( boundingBoxService.addBoundingBox(boundingBox));
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
     
        
    }
    

    @DeleteMapping("")
    public ResponseEntity<Void> deleteBoundingBox(@RequestParam Integer boundingBoxId) {
        try
        {
            boundingBoxService.deleteBoundingBox(boundingBoxId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
