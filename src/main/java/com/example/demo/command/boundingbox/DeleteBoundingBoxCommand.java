package com.example.demo.command.boundingbox;

import com.example.demo.command.Command;
import com.example.demo.model.BoundingBox;
import com.example.demo.service.BoundingBoxService;
import com.example.demo.service.FraudTemplateService;

import jakarta.persistence.EntityNotFoundException;

public class DeleteBoundingBoxCommand implements Command {
    private final BoundingBoxService boundingBoxService;
    private final FraudTemplateService fraudTemplateService;
    
    private final Integer templateId;
    private final Integer boxId;
    
    private BoundingBox deletedBoundingBox;
    
    public DeleteBoundingBoxCommand(
            BoundingBoxService boundingBoxService,
            FraudTemplateService fraudTemplateService,
            Integer templateId,
            Integer boxId) {
        this.boundingBoxService = boundingBoxService;
        this.fraudTemplateService = fraudTemplateService;
        this.templateId = templateId;
        this.boxId = boxId;
    }
    
    @Override
    public void execute() {
        
        if (!fraudTemplateService.existsById(templateId)) {
            System.out.println("Không tìm thấy template với ID: " + templateId);
            return;
        }
        
        
        deletedBoundingBox = boundingBoxService.getBoundingBoxById(boxId);
        if (deletedBoundingBox == null) {
          System.out.println("Không tìm thấy bounding box với ID: " + boxId);
          return;
        }
        
        
        boolean deleted = boundingBoxService.deleteBoundingBoxFromTemplate(templateId, boxId);
        if (!deleted) {
            System.out.println("Không thể xóa bounding box với ID: " + boxId + " từ template với ID: " + templateId);
           return;
        }
    }
    
    @Override
    public void undo()  {
        if (deletedBoundingBox == null) {
            System.out.println("Không thể hoàn tác thao tác xóa vì không có bounding box nào đã bị xóa.");
            return;
        }
        
        deletedBoundingBox.setId(null);
        boundingBoxService.addBoundingBox(deletedBoundingBox);
    }
}