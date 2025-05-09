package com.example.demo.command.fraudlabel;

import com.example.demo.command.Command;
import com.example.demo.model.FraudLabel;
import com.example.demo.service.FraudLabelService;

public class CreateFraudLabelCommand implements Command {
    private FraudLabelService fraudLabelService;
    private FraudLabel fraudLabel;
    private FraudLabel savedFraudLabel;

    public CreateFraudLabelCommand(FraudLabelService fraudLabelService, FraudLabel fraudLabel) {
        this.fraudLabelService = fraudLabelService;
        this.fraudLabel = fraudLabel;
    }

    @Override
    public void execute() {
        savedFraudLabel = fraudLabelService.addFraudLabel(fraudLabel);
    }

    @Override
    public void undo() {
        if (savedFraudLabel != null && savedFraudLabel.getId() != null) {
            fraudLabelService.deleteFraudLabel(savedFraudLabel.getId());
        }
    }
    
    public FraudLabel getResult() {
        return savedFraudLabel;
    }
}