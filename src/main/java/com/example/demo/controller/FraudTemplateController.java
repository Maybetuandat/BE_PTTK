package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.FraudTemplateDTO;
import com.example.demo.model.FraudLabel;
import com.example.demo.model.FraudTemplate;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.FraudLabelService;
import com.example.demo.service.FraudTemplateService;

import jakarta.persistence.EntityNotFoundException;





@RestController
@RequestMapping("/api/fraud-template")
public class FraudTemplateController {


    @Autowired
    FraudTemplateService fraudTemplateService;

    @Autowired
    FraudLabelService fraudLabelService;


    @Autowired
    FileStorageService fileStorageService;
    @GetMapping()
    public ResponseEntity<List<FraudTemplateDTO>> getAllFraudTemplates() {
        return new ResponseEntity<>(fraudTemplateService.getAllFraudTemplatesDTO(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FraudTemplateDTO> getFraudTemplate(
        @PathVariable Integer id // Thêm annotation này
    ) {
        return ResponseEntity.ok(fraudTemplateService.getFraudTemplateDTOById(id));
    }

    @PostMapping()
    public ResponseEntity<List<FraudTemplate>> createTemplate(
        @RequestParam("fraudLabelId") int fraudLabelId,
        @RequestParam("file") MultipartFile[] files
    ) {
       List<FraudTemplate> saveTemplates = new ArrayList<>();
       FraudLabel fraudLabel = fraudLabelService.getFraudLabelById(fraudLabelId);
       for(MultipartFile file : files)
       {
        try{

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String[] listSaveFileName = fileStorageService.saveImage(file, fileName).split("\\s+");

            

            FraudTemplate fraudTemplate = new FraudTemplate();
            fraudTemplate.setName(file.getOriginalFilename());
            fraudTemplate.setImageUrl(fileName);
            fraudTemplate.setFraudLabel(fraudLabel);
            fraudTemplate.setWidth(Integer.parseInt(listSaveFileName[1]));
            fraudTemplate.setHeight(Integer.parseInt(listSaveFileName[2]));
            
            saveTemplates.add(fraudTemplateService.addFraudTemplate(fraudTemplate));


        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
       }
       return new ResponseEntity<>(saveTemplates, HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    public ResponseEntity<FraudTemplate> updateFraudTemplate(
        @PathVariable int id, 
        @ModelAttribute FraudTemplate fraudTemplate,
        @RequestParam(value = "image", required = false ) MultipartFile image
         ) throws IOException {


        FraudTemplate existingTemplate = fraudTemplateService.getFraudTemplateById(id);
        if(existingTemplate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
       // System.out.println(existingTemplate.getFraudLabel());
        
       if (image != null && !image.isEmpty()) {
        String fileName = image.getOriginalFilename();
        String imagePath = fileStorageService.saveImage(image, fileName);  
        existingTemplate.setImageUrl(imagePath);
        existingTemplate.setName(fileName.split("\\.")[0]);
    }
        
        
        return new ResponseEntity<>(fraudTemplateService.updateFraudTemplate(existingTemplate), HttpStatus.OK);
    }
    
    @DeleteMapping()
    public ResponseEntity<String> deleteMultipleFraudTemplate(@RequestBody List<Integer> listId )
    {

      try {
        if (listId == null || listId.isEmpty()) {
            return ResponseEntity.badRequest().body("Danh sách ID không được để trống");
        }

        
       Boolean kt =  fraudTemplateService.deleteFraudTemplates(listId);
        if(kt )
          return ResponseEntity.ok("Deleted successfully");
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi trong quá trình xóa");
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
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Không tìm thấy template với ID: " + id);
        }
        
        fraudTemplateService.deleteFraudTemplate(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
            
    } catch (Exception e) {
        
        System.err.println("Error deleting template with ID " + id + ": " + e.getMessage());
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Có lỗi xảy ra khi xóa template: " + e.getMessage());
    }
}

    @GetMapping("/by-label/{fraudLabelId}")
    public ResponseEntity<List<FraudTemplateDTO>> getFraudTemplatesByLabel(@PathVariable int fraudLabelId) {
    List<FraudTemplateDTO> fraudTemplatesDTO = fraudTemplateService.getFraudTemplatesByLabelId(fraudLabelId);
    
    if (fraudTemplatesDTO.isEmpty()) {
        return ResponseEntity.noContent().build();
    }
    
    return ResponseEntity.ok(fraudTemplatesDTO); 
}



}
