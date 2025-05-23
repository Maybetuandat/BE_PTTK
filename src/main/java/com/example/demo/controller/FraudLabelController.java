package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.command.CommandInvoker;
import com.example.demo.command.fraudlabel.CreateFraudLabelCommand;
import com.example.demo.command.fraudlabel.DeleteFraudLabelCommand;
import com.example.demo.command.fraudlabel.UpdateFraudLabelCommand;
import com.example.demo.model.FraudLabel;
import com.example.demo.service.FraudLabelService;

@RestController
@RequestMapping("/api/fraud-label")
public class FraudLabelController {

    @Autowired
    private FraudLabelService fraudLabelService;
    
    private CommandInvoker commandInvoker = new CommandInvoker();

    @GetMapping("/{id}")
    public ResponseEntity<FraudLabel> getFraudLabel(@PathVariable int id) {
        System.out.println("id: " + id);
        FraudLabel fraudLabelDTO = fraudLabelService.getFraudLabel(id);
        System.out.println("fraudLabel: " + fraudLabelDTO);
        if(fraudLabelDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fraudLabelDTO, HttpStatus.OK);
    }
    
    @GetMapping()
    public ResponseEntity<List<FraudLabel>> getAllFraudLabels() {
        return new ResponseEntity<>(fraudLabelService.getFraudLabels(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<FraudLabel> createFraudLabel(@RequestBody FraudLabel fraudLabel) {
        try {
            CreateFraudLabelCommand command = new CreateFraudLabelCommand(fraudLabelService, fraudLabel);
            commandInvoker.executeCommand(command);
            return new ResponseEntity<>(command.getResult(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   
    
    @PutMapping("/{id}")
    public ResponseEntity<FraudLabel> updateFraudLabel(@PathVariable int id, @RequestBody FraudLabel fraudLabel) {
        try {
            fraudLabel.setId(id);
            UpdateFraudLabelCommand command = new UpdateFraudLabelCommand(fraudLabelService, fraudLabel);
            commandInvoker.executeCommand(command);
            return new ResponseEntity<>(command.getUpdatedFraudLabel(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFraudLabel(@PathVariable int id) {
        try {
          
            
            DeleteFraudLabelCommand command = new DeleteFraudLabelCommand(fraudLabelService, id);
            
            
            String commandId = commandInvoker.executeCommandWithTimeOut(command, 30000);
            
            
            return ResponseEntity.ok().body(Map.of(
                "message", "label deleted",
                "commandId", commandId,
                "undoTimeoutMs", 30000
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Có lỗi xảy ra khi xóa nhãn: " + e.getMessage());
        }
    }


        @PostMapping("/undo/{commandId}")
        public ResponseEntity<?> undoCommand(@PathVariable String commandId) {
            boolean undoSuccessful = commandInvoker.undoCommandWithTimeOut(commandId);
            if (undoSuccessful) {
                return ResponseEntity.ok().body(Map.of(
                    "message", "Return "
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "can't undo this command"
                ));
            }
        }
}