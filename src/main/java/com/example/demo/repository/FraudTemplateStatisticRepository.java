package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.FraudTemplateStatistic;

@Repository
public interface FraudTemplateStatisticRepository  extends JpaRepository<FraudTemplateStatistic, Integer> {
    
    FraudTemplateStatistic findByFraudLabelId(int fraudLabelId);
    
    void deleteByFraudLabelId(int fraudLabelId);

}
