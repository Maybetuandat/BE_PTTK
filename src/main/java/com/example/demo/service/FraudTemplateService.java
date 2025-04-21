package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.example.demo.model.FraudTemplate;


import jakarta.transaction.Transactional;

@Service
public interface FraudTemplateService {

  
    public void deleteFraudTemplate(int id);
    public FraudTemplate getFraudTemplateDTOById(Integer id);
    public List<FraudTemplate> getAllFraudTemplatesDTO();
    public List<FraudTemplate> getFraudTemplatesByLabelId(int fraudLabelId);
    public FraudTemplate getFraudTemplateById(int id);
    public boolean existsById(int id);
    
    public void addFraudTemplate(FraudTemplate fraudTemplate);
    @Transactional
    public Boolean deleteFraudTemplates(List<Integer> listId);
}
