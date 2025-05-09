package com.example.demo.command.fraudtemplate;

import com.example.demo.command.Command;
import com.example.demo.model.FraudTemplate;
import com.example.demo.service.FraudTemplateService;

public class UpdateFraudTemplateCommand implements Command{
    private FraudTemplateService fraudTemplateService;
    private FraudTemplate fraudTemplate;
    private FraudTemplate previousState;

    public UpdateFraudTemplateCommand(FraudTemplateService fraudTemplateService, FraudTemplate fraudTemplate) {
        this.fraudTemplateService = fraudTemplateService;
        this.fraudTemplate = fraudTemplate;
    }

    @Override
    public void execute() {
        if (fraudTemplate.getId() != null) {
            previousState = fraudTemplateService.getFraudTemplateById(fraudTemplate.getId());
            fraudTemplateService.updateFraudTemplate(fraudTemplate);
        }
    }

    @Override
    public void undo() {
        if (previousState != null) {
            fraudTemplateService.updateFraudTemplate(previousState);
        }
    }

    public FraudTemplate getUpdatedFraudTemplate() {
        return fraudTemplateService.getFraudTemplateById(fraudTemplate.getId());
    }

}
