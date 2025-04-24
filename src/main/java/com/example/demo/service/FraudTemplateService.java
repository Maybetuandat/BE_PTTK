package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.example.demo.model.FraudTemplate;


import jakarta.transaction.Transactional;

@Service
public interface FraudTemplateService {

  
    public void deleteFraudTemplate(int id);
    public FraudTemplate getFraudTemplateById(Integer id);
    public List<FraudTemplate> getAllFraudTemplates();
  
    public boolean existsById(int id);
    
    public FraudTemplate addFraudTemplate(FraudTemplate fraudTemplate);
    @Transactional
    public Boolean deleteFraudTemplates(List<Integer> listId);
}
