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


import jakarta.transaction.Transactional;
@Service
public class FraudTemplateServiceImpl  implements FraudTemplateService{

    @Autowired
    private FraudTemplateRepository fraudTemplateRepository;
    @Autowired
    private FileStorageService fileStorageService;
  

    
    
  

    // @Override
    // public FraudTemplate updateFraudTemplate(FraudTemplate fraudTemplate) {
    //      return fraudTemplateRepository.save(fraudTemplate);
    // }


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
    public FraudTemplate getFraudTemplateById(Integer id) {
        return fraudTemplateRepository.findById(id).get();
    }

    @Override
    public List<FraudTemplate> getAllFraudTemplates() {
        List<FraudTemplate> listFraudTemplates =  fraudTemplateRepository.findAll();


      
        return listFraudTemplates;
        
    }

    // @Override
    // public List<FraudTemplate> getFraudTemplatesByLabelId(int fraudLabelId) {
    //     // TODO Auto-generated method stub

        
    //         List<FraudTemplate> listFraudTemplates = fraudTemplateRepository.findByFraudLabelId(fraudLabelId);
           
    //         return listFraudTemplates;
    // }

    @Override
    public FraudTemplate getFraudTemplateById(int id) {
          return fraudTemplateRepository.findById(id).get();
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
              
                System.out.println(fraudTemplate);
                Boolean check = fileStorageService.deleteImage(fraudTemplate.getImageUrl());
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
    public void addFraudTemplate(FraudTemplate fraudTemplate) {
        try
        {
            fraudTemplateRepository.save(fraudTemplate);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
      
    }

    

   
   

}
