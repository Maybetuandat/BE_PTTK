package com.example.demo.command.fraudlabel;

import com.example.demo.service.FraudLabelService;

public class DeleteFraudLabelCommand implements FraudLabelCommand{
    
    
    
    private FraudLabelService fraudLabelService;
    private int id;

    public DeleteFraudLabelCommand(FraudLabelService fraudLabelService, int id) {
        this.fraudLabelService = fraudLabelService;
        this.id = id;
    }

    @Override
    public void execute() {
        fraudLabelService.deleteFraudLabel(id);
    }

  
  

}
