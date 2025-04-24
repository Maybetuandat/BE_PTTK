package com.example.demo.command.fraudlabel;

import com.example.demo.command.Command;
import com.example.demo.model.FraudLabel;
import com.example.demo.service.FraudLabelService;

public class UpdateFraudLabelCommand implements Command {
    private FraudLabelService fraudLabelService;
    private FraudLabel fraudLabel;
    private FraudLabel previousState;

    public UpdateFraudLabelCommand(FraudLabelService fraudLabelService, FraudLabel fraudLabel) {
        this.fraudLabelService = fraudLabelService;
        this.fraudLabel = fraudLabel;
    }

    @Override
    public void execute() {
        if (fraudLabel.getId() != null) {
            previousState = fraudLabelService.getFraudLabel(fraudLabel.getId());
            fraudLabelService.updateFraudLabel(fraudLabel);
        }
    }

    @Override
    public void undo() {
        if (previousState != null) {
            fraudLabelService.updateFraudLabel(previousState);
        }
    }
    
    public FraudLabel getUpdatedFraudLabel() {
        return fraudLabelService.getFraudLabel(fraudLabel.getId());
    }
}