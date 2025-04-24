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
        return new ResponseEntity<>(fraudTemplateService.getAllFraudTemplates(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FraudTemplate> getFraudTemplate(
        @PathVariable Integer id 
    ) {
        return ResponseEntity.ok(fraudTemplateService.getFraudTemplateById(id));
    }

    @PostMapping()
    public ResponseEntity<String> createTemplate(
        
        @RequestParam("file") MultipartFile[] files
    ) throws IOException {

        List<String> listSaveFileName = fileStorageService.saveImage(files);


       
      
       try {
        
                for(String fileName : listSaveFileName)
                {
                    FraudTemplate fraudTemplate = new FraudTemplate();
                    String[] splitToGetHeightAndWidth = fileName.split("\\s+");
                    String imageUrl = splitToGetHeightAndWidth[0];
                    fraudTemplate.setImageUrl(imageUrl);
                    String width = splitToGetHeightAndWidth[1];
                    String height = splitToGetHeightAndWidth[2];
                    // fraudTemplate.setName(splitToGetHeightAndWidth[0]);
                    fraudTemplate.setHeight(Integer.parseInt(height));
                    fraudTemplate.setWidth(Integer.parseInt(width));

                      //http://localhost:8080/images/1681234567890_abc.jpg

                    String fileNameWithoutUrl = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    System.out.println("File name without URL: " + fileNameWithoutUrl);
                    fraudTemplate.setName(fileNameWithoutUrl);

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

    
    @DeleteMapping()
    public ResponseEntity<String> deleteMultipleFraudTemplate(@RequestBody List<Integer> listId )
    {

      try {
        if (listId == null || listId.isEmpty()) {
            return ResponseEntity.badRequest().body("Danh sách ID không được để trống");
        }

        
       Boolean kt =  fraudTemplateService.deleteFraudTemplates(listId);
       System.out.println(listId);
       System.out.println(kt);
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

//     @GetMapping("/by-label/{fraudLabelId}")
//     public ResponseEntity<List<FraudTemplate>> getFraudTemplatesByLabel(@PathVariable int fraudLabelId) {
//     List<FraudTemplate> fraudTemplatesDTO = fraudTemplateService.getFraudTemplatesByLabelId(fraudLabelId);
    
//     if (fraudTemplatesDTO.isEmpty()) {
//         return ResponseEntity.noContent().build();
//     }
    
//     return ResponseEntity.ok(fraudTemplatesDTO); 
// }



}
