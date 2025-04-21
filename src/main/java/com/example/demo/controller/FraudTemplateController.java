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
    public ResponseEntity<List<FraudTemplate>> getAllFraudTemplates() {
        return new ResponseEntity<>(fraudTemplateService.getAllFraudTemplatesDTO(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FraudTemplate> getFraudTemplate(
        @PathVariable Integer id 
    ) {
        return ResponseEntity.ok(fraudTemplateService.getFraudTemplateDTOById(id));
    }

    @PostMapping()
    public ResponseEntity<String> createTemplate(
        @RequestParam("fraudLabelId") int fraudLabelId,
        @RequestParam("file") MultipartFile[] files
    ) throws IOException {

        List<String> listSaveFileName = fileStorageService.saveImage(files);


       
       FraudLabel fraudLabel = fraudLabelService.getFraudLabelById(fraudLabelId);
       try {
        
                for(String fileName : listSaveFileName)
                {
                    FraudTemplate fraudTemplate = new FraudTemplate();
                    fraudTemplate.setFraudLabel(fraudLabel);
                    fraudTemplate.setImageUrl(fileName);
                    String[] firstSplit = fileName.split("\\."); 
                    String[] secondSplit = firstSplit[0].split("/"); 
                    String lastPart = secondSplit[secondSplit.length - 1]; 
                    fraudTemplate.setName(lastPart);

                   fraudTemplateService.addFraudTemplate(fraudTemplate);
                }
       } catch (Exception e) {
        // TODO: handle exception
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Đã xảy ra lỗi trong quá trình thêm template: " + e.getMessage());
       }
      
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Thêm template thành công");
       
       
    }



    // @PutMapping("/{id}")
    // public ResponseEntity<FraudTemplate> updateFraudTemplate(
    //     @PathVariable int id, 
    //     @ModelAttribute FraudTemplate fraudTemplate,
    //     @RequestParam(value = "image", required = false ) MultipartFile image
    //      ) throws IOException {


    //     FraudTemplate existingTemplate = fraudTemplateService.getFraudTemplateById(id);
    //     if(existingTemplate == null) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //    // System.out.println(existingTemplate.getFraudLabel());
        
    //    if (image != null && !image.isEmpty()) {
    //     String fileName = image.getOriginalFilename();
    //     String imagePath = fileStorageService.saveImage(image, fileName);  
    //     existingTemplate.setImageUrl(imagePath);
    //     existingTemplate.setName(fileName.split("\\.")[0]);
    // }
        
        
    //     return new ResponseEntity<>(fraudTemplateService.updateFraudTemplate(existingTemplate), HttpStatus.OK);
    // }
    
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
    public ResponseEntity<List<FraudTemplate>> getFraudTemplatesByLabel(@PathVariable int fraudLabelId) {
    List<FraudTemplate> fraudTemplatesDTO = fraudTemplateService.getFraudTemplatesByLabelId(fraudLabelId);
    
    if (fraudTemplatesDTO.isEmpty()) {
        return ResponseEntity.noContent().build();
    }
    
    return ResponseEntity.ok(fraudTemplatesDTO); 
}



}
