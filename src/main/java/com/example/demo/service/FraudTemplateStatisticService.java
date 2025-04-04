package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.FraudTemplateStatisticDTO;
import com.example.demo.model.FraudLabel;
import com.example.demo.model.FraudTemplateStatistic;
import com.example.demo.observer.FraudTemplateObserver;

import jakarta.transaction.Transactional;

@Service
public interface FraudTemplateStatisticService  extends FraudTemplateObserver{

    @Transactional
    public void createFraudLabelStatistics();

    public FraudTemplateStatistic getFraudLabelStatisticById(Integer fraudLabelId);
    public FraudTemplateStatistic upFraudLabelStatistic(Integer fraudLabelId);
    public List<FraudTemplateStatistic> getAllStatistics();
    public void deleteStatistic(Integer fraudLabelId) ;
    public List<FraudTemplateStatisticDTO> getAllStatisticsDTO();
}
