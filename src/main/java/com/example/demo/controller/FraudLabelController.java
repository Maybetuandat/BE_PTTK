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

import com.example.demo.command.fraudlabel.CreateFraudLabelCommand;
import com.example.demo.command.fraudlabel.DeleteFraudLabelCommand;

import com.example.demo.command.fraudlabel.FraudLabelCommandInvoker;
import com.example.demo.command.fraudlabel.UpdateFraudLabelCommand;
import com.example.demo.dto.FraudLabelDTO;

import com.example.demo.model.FraudLabel;
import com.example.demo.service.FraudLabelService;

@RestController
@RequestMapping("/api/fraud-label")
public class FraudLabelController {

    @Autowired
    FraudLabelService fraudLabelService;

    private FraudLabelCommandInvoker invoker = new FraudLabelCommandInvoker();


    @GetMapping("/{id}")
    public ResponseEntity<FraudLabelDTO> getFraudLabel(@PathVariable int id) {
        
        System.out.println("id: " + id);
        FraudLabelDTO fraudLabelDTO = fraudLabelService.getFraudLabelDTO(id);
        System.out.println("fraudLabel: " + fraudLabelDTO);
        if(fraudLabelDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fraudLabelDTO, HttpStatus.OK);
        
    }
    @GetMapping()
    public ResponseEntity<List<FraudLabelDTO>> getAllFraudLabels() {
        return new ResponseEntity<>(fraudLabelService.getFraudLabelsDTO(), HttpStatus.OK);
    }



    @PostMapping()
    public ResponseEntity<FraudLabel> createFraudLabel(@ModelAttribute  FraudLabel fraudLabel) {

        
        CreateFraudLabelCommand createCommand = new CreateFraudLabelCommand(fraudLabelService, fraudLabel);
        invoker.addCommand(createCommand); 
        invoker.executeCommands();
        return new ResponseEntity<>(fraudLabel, HttpStatus.CREATED);
    }

   

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFraudLabel(@PathVariable int id) {
        
        DeleteFraudLabelCommand deleteCommand = new DeleteFraudLabelCommand(fraudLabelService, id);
        invoker.addCommand(deleteCommand);
        invoker.executeCommands();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}")
    public ResponseEntity<FraudLabel> updateFraudLabel(@PathVariable int id, @RequestBody FraudLabel fraudLabel) {
        fraudLabel.setId(id);
      
        UpdateFraudLabelCommand updateFraudLabelCommand = new UpdateFraudLabelCommand(fraudLabelService, fraudLabel);
        invoker.addCommand(updateFraudLabelCommand);
        invoker.executeCommands();

        return new ResponseEntity<>(fraudLabel, HttpStatus.OK);
    }
}
