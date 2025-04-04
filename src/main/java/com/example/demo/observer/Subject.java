package com.example.demo.observer;

import com.example.demo.model.FraudLabel;

public interface Subject {
    void attach(FraudTemplateObserver observer);
    void detach(FraudTemplateObserver observer);
    void notifyOnCreate(FraudLabel fraudLabel);
    void notifyOnDelete(FraudLabel fraudLabel);

}
