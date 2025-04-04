package com.example.demo.command.fraudlabel;

import com.example.demo.model.FraudLabel;
import com.example.demo.service.FraudLabelService;

public class UpdateFraudLabelCommand  implements FraudLabelCommand{

    private FraudLabelService fraudLabelService;
    private FraudLabel fraudLabel;
    public UpdateFraudLabelCommand(FraudLabelService fraudLabelService, FraudLabel fraudLabel)
    {
            this.fraudLabelService = fraudLabelService;
            this.fraudLabel = fraudLabel;
    }

    @Override
    public void execute() {
         fraudLabelService.updateFraudLabel(fraudLabel);
    }


}
