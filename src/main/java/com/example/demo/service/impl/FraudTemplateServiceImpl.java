package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.demo.model.FraudLabel;
import com.example.demo.model.FraudTemplate;

import com.example.demo.repository.FraudTemplateRepository;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.FraudTemplateService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
@Service
public class FraudTemplateServiceImpl  implements FraudTemplateService{


    
    private final String UPLOAD_DIR = "images/";
    @Autowired
    private FraudTemplateRepository fraudTemplateRepository;
    @Autowired
    private FileStorageService fileStorageService;
  

    @Override
    public void deleteFraudTemplate(int id) {
        try
        {
            FraudTemplate fraudTemplate = fraudTemplateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("FraudTemplate not found"));
          
            fraudTemplateRepository.delete(fraudTemplate);   
                     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }



    @Override
    public List<FraudTemplate> getAllFraudTemplates() {
        List<FraudTemplate> listFraudTemplates =  fraudTemplateRepository.findAll();

       

      
        return listFraudTemplates;
        
    }

  

    @Override
    public FraudTemplate getFraudTemplateById(Integer id) {
          FraudTemplate fraudTemplate = fraudTemplateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("FraudTemplate not found"));
        
        return fraudTemplate;
          
    }

    @Override
    public boolean existsById(int id) {
         return fraudTemplateRepository.existsById(id);
    }

    @Override
    @Transactional
    public Boolean deleteFraudTemplates(List<Integer> listId) {
        

        try {
            for (Integer id : listId) {
                FraudTemplate fraudTemplate = fraudTemplateRepository.findById(id).get();
              
             //   System.out.println(fraudTemplate);
                
                String imageUrl = UPLOAD_DIR + fraudTemplate.getName();
              //  System.out.println(imageUrl);
                Boolean check = fileStorageService.deleteImage(imageUrl);
                System.out.println(check);
                fraudTemplateRepository.deleteById(id);
              
            }
        } catch (Exception e) {
            // TODO: handle exception
                return false;
        }
        return true;
    }

   
  

  

 


    @Override
    public FraudTemplate addFraudTemplate(FraudTemplate fraudTemplate) {
        try
        {
            return fraudTemplateRepository.save(fraudTemplate);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            return null;
        }
      
    }



        @Override
        @Transactional
        public FraudTemplate updateFraudTemplate(FraudTemplate fraudTemplate) {
            if (fraudTemplate == null || fraudTemplate.getId() == null) {
                throw new IllegalArgumentException("FraudTemplate hoặc ID không được để trống");
            }
            
            if (!existsById(fraudTemplate.getId())) {
                throw new EntityNotFoundException("Không tìm thấy FraudTemplate với ID " + fraudTemplate.getId());
            }
            
            return fraudTemplateRepository.save(fraudTemplate);
        }
    

   
   

}
