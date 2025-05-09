package com.example.demo.command.fraudtemplate;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.command.Command;
import com.example.demo.model.FraudTemplate;
import com.example.demo.service.FraudTemplateService;

public class DeleteMultipleFraudTemplatesCommand implements Command {
    private FraudTemplateService fraudTemplateService;
    private List<Integer> templateIds;
    private List<FraudTemplate> deletedTemplates;

    public DeleteMultipleFraudTemplatesCommand(FraudTemplateService fraudTemplateService, List<Integer> templateIds) {
        this.fraudTemplateService = fraudTemplateService;
        this.templateIds = templateIds;
        this.deletedTemplates = new ArrayList<>();
    }

    @Override
    public void execute() {
        if (templateIds != null && !templateIds.isEmpty()) {
            // Lưu trạng thái trước khi xóa
            for (Integer id : templateIds) {
                if (fraudTemplateService.existsById(id)) {
                    deletedTemplates.add(fraudTemplateService.getFraudTemplateById(id));
                }
            }
            
            
            fraudTemplateService.deleteFraudTemplates(templateIds);
        }
    }

    @Override
    public void undo() {
        if (deletedTemplates != null && !deletedTemplates.isEmpty()) {
            for (FraudTemplate template : deletedTemplates) {
                template.setId(null); 
                fraudTemplateService.addFraudTemplate(template);
            }
        }
    }
}