package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.FraudTemplateStatisticDTO;
import com.example.demo.dto.mapper.FraudTemplateStatisticMapper;
import com.example.demo.model.FraudLabel;
import com.example.demo.model.FraudTemplateStatistic;
import com.example.demo.repository.FraudLabelRepository;
import com.example.demo.repository.FraudTemplateStatisticRepository;
import com.example.demo.repository.FraudTemplateRepository;
import com.example.demo.service.FraudTemplateStatisticService;

@Service
public class FraudStatisticServiceImpl implements FraudTemplateStatisticService{

    @Autowired
    FraudLabelRepository fraudLabelRepository;

    @Autowired
    FraudTemplateRepository   fraudTemplateRepository;;

    @Autowired
    FraudTemplateStatisticRepository fraudTemplateStatisticRepository;
    
    
    
    @Override
    public void createFraudLabelStatistics() {
       List<FraudLabel> fraudLabels = fraudLabelRepository.findAll();
       for(FraudLabel fraudLabel : fraudLabels)
       {
            int templateCount = fraudTemplateRepository.countByFraudLabel(fraudLabel);
            
            FraudTemplateStatistic fraudLabelStatistic = fraudTemplateStatisticRepository.findByFraudLabelId(fraudLabel.getId());
            if(fraudLabelStatistic == null)
            {
                fraudLabelStatistic = new FraudTemplateStatistic();
                fraudLabelStatistic.setFraudLabel(fraudLabel);
                fraudLabelStatistic.setTemplateCount(templateCount);
                fraudTemplateStatisticRepository.save(fraudLabelStatistic);
            }
            else
            {
                fraudLabelStatistic.setTemplateCount(templateCount);
                fraudTemplateStatisticRepository.save(fraudLabelStatistic);
            }
       }
    }

    @Override
    public List<FraudTemplateStatistic> getAllStatistics() {
          return fraudTemplateStatisticRepository.findAll();
    }

   

    @Override
    public FraudTemplateStatistic getFraudLabelStatisticById(Integer fraudLabelId) {
        FraudTemplateStatistic fraudLabelStatistic = fraudTemplateStatisticRepository.findByFraudLabelId(fraudLabelId);
        if(fraudLabelStatistic == null)
        {
            throw new RuntimeException("Fraud Label Statistic not found for id: " + fraudLabelId);
        }
        return fraudLabelStatistic;
    }

    @Override
    public FraudTemplateStatistic upFraudLabelStatistic(Integer fraudLabelId) {
        FraudTemplateStatistic fraudLabelStatistic = fraudTemplateStatisticRepository.findByFraudLabelId(fraudLabelId);
        FraudLabel fraudLabel = fraudLabelRepository.findById(fraudLabelId).orElse(null);
        if(fraudLabel == null)
        {
            throw new RuntimeException("Fraud Label not found for id: " + fraudLabelId);
        }
        if(fraudLabelStatistic == null)
        {
            throw new RuntimeException("Fraud Label Statistic not found for id: " + fraudLabelId);
        }
        int templateCount = fraudTemplateRepository.countByFraudLabel(fraudLabel);
        fraudLabelStatistic.setTemplateCount(templateCount);
        return fraudTemplateStatisticRepository.save(fraudLabelStatistic);
    }

    @Override
    public void deleteStatistic(Integer fraudLabelId) {
        FraudTemplateStatistic fraudLabelStatistic = fraudTemplateStatisticRepository.findByFraudLabelId(fraudLabelId);
        if(fraudLabelStatistic == null)
        {
            throw new RuntimeException("Fraud Label Statistic not found for id: " + fraudLabelId);
        }
        fraudTemplateStatisticRepository.delete(fraudLabelStatistic);
    }

    @Override
    public List<FraudTemplateStatisticDTO> getAllStatisticsDTO() {
        List<FraudTemplateStatistic> fraudLabelStatistics = fraudTemplateStatisticRepository.findAll();
        List<FraudTemplateStatisticDTO> fraudLabelStatisticDTOs = new ArrayList<>();
        for(FraudTemplateStatistic fraudLabelStatistic : fraudLabelStatistics)
        {
            FraudTemplateStatisticDTO dto = FraudTemplateStatisticMapper.mapToDTO(fraudLabelStatistic);
            fraudLabelStatisticDTOs.add(dto);
        }
        return fraudLabelStatisticDTOs;
    }

    @Override
    public void updateOnCreate(FraudLabel fraudLabel) {
       FraudTemplateStatistic statistic = fraudTemplateStatisticRepository.findByFraudLabelId(fraudLabel.getId());
       if(statistic == null)
       {
        statistic = new FraudTemplateStatistic();
        statistic.setFraudLabel(fraudLabel);
        statistic.setTemplateCount(0);     
        }
        statistic.setTemplateCount(statistic.getTemplateCount() + 1);
        fraudTemplateStatisticRepository.save(statistic);
        
       
    }

    @Override
    public void updateOnDelete(FraudLabel fraudLabel) {
        FraudTemplateStatistic statistic = fraudTemplateStatisticRepository.findByFraudLabelId(fraudLabel.getId());
        if(statistic == null)
        {
            throw new RuntimeException("Fraud Label Statistic not found for id: " + fraudLabel.getId());
        }
        statistic.setTemplateCount(Math.max(0, statistic.getTemplateCount() - 1));
        fraudTemplateStatisticRepository.save(statistic);
    }

}
