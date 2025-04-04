package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.FraudTemplateDTO;
import com.example.demo.dto.mapper.FraudTemplateMapper;
import com.example.demo.model.FraudLabel;
import com.example.demo.model.FraudTemplate;
import com.example.demo.observer.FraudTemplateObserver;
import com.example.demo.repository.FraudTemplateRepository;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.FraudTemplateService;
import com.example.demo.service.FraudTemplateStatisticService;

import jakarta.transaction.Transactional;
@Service
public class FraudTemplateServiceImpl  implements FraudTemplateService{

    private final FraudTemplateRepository fraudTemplateRepository;
    private final FileStorageService fileStorageService;
    private final List<FraudTemplateObserver> observers = new ArrayList<>();

    
    public FraudTemplateServiceImpl(FraudTemplateRepository fraudTemplateRepository, 
                                    FileStorageService fileStorageService, 
                                    FraudTemplateStatisticService fraudTemplateStatisticService) {
        this.fraudTemplateRepository = fraudTemplateRepository;
        this.fileStorageService = fileStorageService;
        attach(fraudTemplateStatisticService);
    }
    @Override
    public FraudTemplate addFraudTemplate(FraudTemplate fraudTemplate) {
        FraudTemplate savedFraudTemplate = fraudTemplateRepository.save(fraudTemplate);
         

        notifyOnCreate(savedFraudTemplate.getFraudLabel());
        return savedFraudTemplate;
    }

    @Override
    public FraudTemplate updateFraudTemplate(FraudTemplate fraudTemplate) {
         return fraudTemplateRepository.save(fraudTemplate);
    }


    @Override
    public void deleteFraudTemplate(int id) {
        try
        {
            FraudTemplate fraudTemplate = fraudTemplateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("FraudTemplate not found"));
            notifyOnDelete(fraudTemplate.getFraudLabel());
            fraudTemplateRepository.delete(fraudTemplate);   
                     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    @Override
    public FraudTemplateDTO getFraudTemplateDTOById(Integer id) {
        return FraudTemplateMapper.mapToDTO(fraudTemplateRepository.findById(id).get());
    }

    @Override
    public List<FraudTemplateDTO> getAllFraudTemplatesDTO() {
        List<FraudTemplate> listFraudTemplates =  fraudTemplateRepository.findAll();


        List<FraudTemplateDTO> listFraudTemplateDTO = new ArrayList<FraudTemplateDTO>();
        for(FraudTemplate fraudTemplate : listFraudTemplates)
        {
            listFraudTemplateDTO.add(FraudTemplateMapper.mapToDTO(fraudTemplate));
        }
        return listFraudTemplateDTO;
        
    }

    @Override
    public List<FraudTemplateDTO> getFraudTemplatesByLabelId(int fraudLabelId) {
        // TODO Auto-generated method stub

        
            List<FraudTemplate> listFraudTemplates = fraudTemplateRepository.findByFraudLabelId(fraudLabelId);
            List<FraudTemplateDTO> listFraudTemplateDTO = new ArrayList<FraudTemplateDTO>();
            for(FraudTemplate fraudTemplate : listFraudTemplates)
            {
                listFraudTemplateDTO.add(FraudTemplateMapper.mapToDTO(fraudTemplate));
            }
            return listFraudTemplateDTO;
    }

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
                fileStorageService.deleteImage(fraudTemplate.getImageUrl());
                fraudTemplateRepository.deleteById(id);
                notifyOnDelete(fraudTemplate.getFraudLabel());
            }
        } catch (Exception e) {
            // TODO: handle exception
                return false;
        }
        return true;
    }

    @Override
    public void attach(FraudTemplateObserver observer) {
        observers.add(observer);    
    }

    @Override
    public void detach(FraudTemplateObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyOnCreate(FraudLabel fraudLabel) {
        
        for (FraudTemplateObserver observer : observers) {
            observer.updateOnCreate(fraudLabel);
        }
       
    }

    @Override
    public void notifyOnDelete(FraudLabel fraudLabel) {
        
        for (FraudTemplateObserver observer : observers) {
            observer.updateOnDelete(fraudLabel);
        }
    }

    

   
   

}
