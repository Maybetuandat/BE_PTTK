package com.example.demo.command.fraudlabel;

import com.example.demo.model.FraudLabel;
import com.example.demo.service.FraudLabelService;

public class CreateFraudLabelCommand  implements FraudLabelCommand{


    private FraudLabel fraudLabel;
    private FraudLabelService fraudLabelService;

    public CreateFraudLabelCommand(FraudLabelService fraudLabelService, FraudLabel fraudLabel)
    {
            this.fraudLabelService = fraudLabelService;
            this.fraudLabel = fraudLabel;
    }
    @Override
    public void execute() {
         fraudLabelService.addFraudLabel(fraudLabel);
    }

}
