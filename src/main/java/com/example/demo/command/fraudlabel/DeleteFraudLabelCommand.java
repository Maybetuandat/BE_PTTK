package com.example.demo.command.fraudlabel;



import com.example.demo.command.Command;
import com.example.demo.model.FraudLabel;
import com.example.demo.service.FraudLabelService;

import lombok.extern.java.Log;

import org.slf4j.Logger;
public class DeleteFraudLabelCommand implements Command {
    private FraudLabelService fraudLabelService;
    private int fraudLabelId;
    private FraudLabel deletedFraudLabel;

    public DeleteFraudLabelCommand(FraudLabelService fraudLabelService, int fraudLabelId) {
        this.fraudLabelService = fraudLabelService;
        this.fraudLabelId = fraudLabelId;
    }

    @Override
    public void execute() {
        deletedFraudLabel = fraudLabelService.getFraudLabel(fraudLabelId);
        if (deletedFraudLabel != null) {
            fraudLabelService.deleteFraudLabel(fraudLabelId);
        }
    }

    @Override
    public void undo() {

        if (deletedFraudLabel != null) {
            System.out.println("Undoing delete of FraudLabel: " + deletedFraudLabel);
           
            fraudLabelService.addFraudLabel(deletedFraudLabel);
        }
        System.out.println("Undoing delete of FraudLabel: " + deletedFraudLabel);
    }
}