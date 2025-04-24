package com.example.demo.command.fraudtemplate;

import com.example.demo.command.Command;
import com.example.demo.model.FraudTemplate;
import com.example.demo.service.FraudTemplateService;

public class DeleteFraudTemplateCommand implements Command {
    private FraudTemplateService fraudTemplateService;
    private int fraudTemplateId;
    private FraudTemplate deletedTemplate;

    public DeleteFraudTemplateCommand(FraudTemplateService fraudTemplateService, int fraudTemplateId) {
        this.fraudTemplateService = fraudTemplateService;
        this.fraudTemplateId = fraudTemplateId;
    }

    @Override
    public void execute() {
        if (fraudTemplateService.existsById(fraudTemplateId)) {
            deletedTemplate = fraudTemplateService.getFraudTemplateById(fraudTemplateId);
            fraudTemplateService.deleteFraudTemplate(fraudTemplateId);
        }
    }

    @Override
    public void undo() {
        if (deletedTemplate != null) {
            deletedTemplate.setId(null); 
            fraudTemplateService.addFraudTemplate(deletedTemplate);
        }
    }
}