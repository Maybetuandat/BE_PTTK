package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.FraudTemplateDTO;
import com.example.demo.model.FraudTemplate;
import com.example.demo.observer.Subject;

import jakarta.transaction.Transactional;

@Service
public interface FraudTemplateService  extends Subject{

    @Transactional
    public FraudTemplate addFraudTemplate(FraudTemplate fraudTemplate);
    public FraudTemplate updateFraudTemplate(FraudTemplate fraudTemplate);
    @Transactional
    public void deleteFraudTemplate(int id);
    public FraudTemplateDTO getFraudTemplateDTOById(Integer id);
    public List<FraudTemplateDTO> getAllFraudTemplatesDTO();
    public List<FraudTemplateDTO> getFraudTemplatesByLabelId(int fraudLabelId);
    public FraudTemplate getFraudTemplateById(int id);
    public boolean existsById(int id);
    
    @Transactional
    public Boolean deleteFraudTemplates(List<Integer> listId);
}
