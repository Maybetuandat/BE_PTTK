package com.example.demo.observer;

import com.example.demo.model.FraudLabel;

public interface FraudTemplateObserver {
    void  updateOnCreate(FraudLabel fraudLabel);
    void  updateOnDelete(FraudLabel fraudLabel);
    
}