package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.example.demo.model.FraudLabel;

@Service
public interface FraudLabelService {

    
    public FraudLabel addFraudLabel(FraudLabel fraudLabel);
    public FraudLabel updateFraudLabel(FraudLabel fraudLabel);
    public void deleteFraudLabel(int id);
    public FraudLabel getFraudLabel(int id);
    
    public FraudLabel getFraudLabelById(int id);
    public List<FraudLabel> getFraudLabels();
}
