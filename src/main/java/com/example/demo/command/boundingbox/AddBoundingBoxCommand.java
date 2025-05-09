package com.example.demo.command.boundingbox;

import com.example.demo.command.Command;
import com.example.demo.model.BoundingBox;
import com.example.demo.model.FraudLabel;
import com.example.demo.model.FraudTemplate;
import com.example.demo.service.BoundingBoxService;
import com.example.demo.service.FraudLabelService;
import com.example.demo.service.FraudTemplateService;

import jakarta.persistence.EntityNotFoundException;

public class AddBoundingBoxCommand implements Command {
    private final BoundingBoxService boundingBoxService;
    private final FraudTemplateService fraudTemplateService;
    private final FraudLabelService fraudLabelService;
    
    private final Integer fraudTemplateId;
    private final Integer xPixel;
    private final Integer yPixel;
    private final Integer widthPixel;
    private final Integer heightPixel;
    private final Integer fraudLabelId;
    
    private BoundingBox savedBoundingBox;
    
    public AddBoundingBoxCommand(
            BoundingBoxService boundingBoxService,
            FraudTemplateService fraudTemplateService,
            FraudLabelService fraudLabelService,
            Integer fraudTemplateId,
            Integer xPixel,
            Integer yPixel,
            Integer widthPixel,
            Integer heightPixel,
            Integer fraudLabelId) {
        this.boundingBoxService = boundingBoxService;
        this.fraudTemplateService = fraudTemplateService;
        this.fraudLabelService = fraudLabelService;
        this.fraudTemplateId = fraudTemplateId;
        this.xPixel = xPixel;
        this.yPixel = yPixel;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.fraudLabelId = fraudLabelId;
    }
    
    @Override
    public void execute()  {
        
        if (!fraudTemplateService.existsById(fraudTemplateId)) {
            System.out.println("Không tìm thấy template với ID: " + fraudTemplateId);
            return;
        }
        
        
        FraudTemplate fraudTemplate = fraudTemplateService.getFraudTemplateById(fraudTemplateId);
        
        
        FraudLabel fraudLabel = fraudLabelService.getFraudLabelById(fraudLabelId);
        
        
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setXPixel(xPixel);
        boundingBox.setYPixel(yPixel);
        boundingBox.setWidthPixel(widthPixel);
        boundingBox.setHeightPixel(heightPixel);
        boundingBox.setFraudTemplate(fraudTemplate);
        boundingBox.setFraudLabel(fraudLabel);
        
        
        if (boundingBox.getXPixel() != null && boundingBox.getYPixel() != null 
                && boundingBox.getWidthPixel() != null && boundingBox.getHeightPixel() != null) {
            boundingBox.convertToYoloParameter();
        } else {
           System.out.println("Không thể chuyển đổi bounding box sang tham số YOLO vì một trong các tham số là null");
           return;
        }
        
        
        savedBoundingBox = boundingBoxService.addBoundingBox(boundingBox);
    }
    
    @Override
    public void undo()  {
        if (savedBoundingBox == null) {
            System.out.println("Không thể hoàn tác thao tác thêm vì không có bounding box nào đã được thêm.");
            return;
        }
        
        
        boundingBoxService.deleteBoundingBoxFromTemplate(
                savedBoundingBox.getFraudTemplate().getId(), 
                savedBoundingBox.getId());
    }
    
    public BoundingBox getSavedBoundingBox() {
        return savedBoundingBox;
    }
}