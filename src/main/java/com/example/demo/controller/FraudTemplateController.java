package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.command.CommandInvoker;
import com.example.demo.command.boundingbox.AddBoundingBoxCommand;
import com.example.demo.command.boundingbox.DeleteBoundingBoxCommand;
import com.example.demo.command.fraudtemplate.CreateFraudTemplateCommand;
import com.example.demo.command.fraudtemplate.DeleteFraudTemplateCommand;
import com.example.demo.command.fraudtemplate.DeleteMultipleFraudTemplatesCommand;
import com.example.demo.model.BoundingBox;
import com.example.demo.model.FraudLabel;
import com.example.demo.model.FraudTemplate;
import com.example.demo.service.BoundingBoxService;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.FraudLabelService;
import com.example.demo.service.FraudTemplateService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/fraud-template")
public class FraudTemplateController {

    @Autowired
    BoundingBoxService boundingBoxService;
    @Autowired
    private FraudTemplateService fraudTemplateService;

    @Autowired
    private FraudLabelService fraudLabelService;

    @Autowired
    private FileStorageService fileStorageService;
    
    private CommandInvoker commandInvoker = new CommandInvoker();

    @GetMapping()
    public ResponseEntity<List<FraudTemplate>> getAllFraudTemplates() {
        return new ResponseEntity<>(fraudTemplateService.getAllFraudTemplates(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FraudTemplate> getFraudTemplate(@PathVariable Integer id) {
        System.out.println(">> Controller called with id = " + id);
        FraudTemplate fraudTemplate = fraudTemplateService.getFraudTemplateById(id);
        if (fraudTemplate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
        }
        System.out.println("FraudTemplate: " + fraudTemplate);
        return ResponseEntity.ok(fraudTemplate);
    }

    @PostMapping()
    public ResponseEntity<String> createTemplate(@RequestParam("file") MultipartFile[] files) {
        try {
            CreateFraudTemplateCommand command = new CreateFraudTemplateCommand(
                fraudTemplateService, fileStorageService, files);
            commandInvoker.executeCommand(command);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm template thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Đã xảy ra lỗi trong quá trình thêm template: " + e.getMessage());
        }
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteMultipleFraudTemplate(@RequestBody List<Integer> listId) {
        try {
            if (listId == null || listId.isEmpty()) {
                return ResponseEntity.badRequest().body("Danh sách ID không được để trống");
            }

            DeleteMultipleFraudTemplatesCommand command = new DeleteMultipleFraudTemplatesCommand(
                fraudTemplateService, listId);
            commandInvoker.executeCommand(command);
            return ResponseEntity.ok("Deleted successfully");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Lỗi ràng buộc dữ liệu: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy một số ID trong danh sách");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi trong quá trình xóa: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFraudTemplate(@PathVariable("id") int id) {
        try {
            if (!fraudTemplateService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy template với ID: " + id);
            }
            
            DeleteFraudTemplateCommand command = new DeleteFraudTemplateCommand(
                fraudTemplateService, id);
            commandInvoker.executeCommand(command);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            System.err.println("Error deleting template with ID " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Có lỗi xảy ra khi xóa template: " + e.getMessage());
        }
    }
    
    // Optional: Endpoint để hoàn tác thao tác cuối cùng


   @PostMapping("/{id}/bounding-box")
    public ResponseEntity<?> addBoundingBoxToTemplate(
            @PathVariable("id") Integer fraudTemplateId,
            @RequestParam Integer xPixel,
            @RequestParam Integer yPixel,
            @RequestParam Integer widthPixel,
            @RequestParam Integer heightPixel,
            @RequestParam Integer fraudLabelId) {
        
        try {
            
            AddBoundingBoxCommand command = new AddBoundingBoxCommand(
                boundingBoxService,
                fraudTemplateService,
                fraudLabelService,
                fraudTemplateId,
                xPixel,
                yPixel,
                widthPixel,
                heightPixel,
                fraudLabelId
            );
            
            commandInvoker.executeCommand(command);
            
            
            BoundingBox savedBoundingBox = command.getSavedBoundingBox();
            
            return ResponseEntity.status(HttpStatus.OK).body(savedBoundingBox);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi khi thêm bounding box: " + e.getMessage());
        }
    }
  @DeleteMapping("/{templateId}/bounding-box/{boxId}")
    public ResponseEntity<?> deleteBoundingBoxFromTemplate(
            @PathVariable("templateId") Integer templateId,
            @PathVariable("boxId") Integer boxId) {
        
        try {
            
            DeleteBoundingBoxCommand command = new DeleteBoundingBoxCommand(
                boundingBoxService,
                fraudTemplateService,
                templateId,
                boxId
            );
            
            commandInvoker.executeCommand(command);
            
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error deleting bounding box: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi khi xóa bounding box: " + e.getMessage());
        }
    }
    
}
               
