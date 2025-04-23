package com.example.demo.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;



import com.example.demo.model.FraudLabel;
import com.example.demo.service.FraudLabelService;

@RestController
@RequestMapping("/api/fraud-label")
public class FraudLabelController {

    @Autowired
    FraudLabelService fraudLabelService;

   


    @GetMapping("/{id}")
    public ResponseEntity<FraudLabel> getFraudLabel(@PathVariable int id) {
        
        System.out.println("id: " + id);
        FraudLabel fraudLabelDTO = fraudLabelService.getFraudLabelDTO(id);
        System.out.println("fraudLabel: " + fraudLabelDTO);
        if(fraudLabelDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fraudLabelDTO, HttpStatus.OK);
        
    }
    @GetMapping()
    public ResponseEntity<List<FraudLabel>> getAllFraudLabels() {
        return new ResponseEntity<>(fraudLabelService.getFraudLabelsDTO(), HttpStatus.OK);
    }



    @PostMapping()
    public ResponseEntity<FraudLabel> createFraudLabel(@RequestBody  FraudLabel fraudLabel) {

        
       FraudLabel saveFraudLabel = fraudLabelService.addFraudLabel(fraudLabel);
        return new ResponseEntity<>(saveFraudLabel, HttpStatus.CREATED);
    }

   

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFraudLabel(@PathVariable int id) {
        
        fraudLabelService.deleteFraudLabel(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}")
    public ResponseEntity<FraudLabel> updateFraudLabel(@PathVariable int id, @RequestBody FraudLabel fraudLabel) {
        fraudLabel.setId(id);
      
        FraudLabel updatedFraudLabel = fraudLabelService.updateFraudLabel(fraudLabel);

        return new ResponseEntity<>(updatedFraudLabel, HttpStatus.OK);
    }
}
