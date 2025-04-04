package com.example.demo.dto.mapper;

import com.example.demo.dto.FraudTemplateStatisticDTO;
import com.example.demo.model.FraudTemplateStatistic;

public class FraudTemplateStatisticMapper {

    public static FraudTemplateStatisticDTO mapToDTO(FraudTemplateStatistic fraudLabelStatistic)
    {
        if (fraudLabelStatistic == null) {
            return null;
        }

        FraudTemplateStatisticDTO dto = new FraudTemplateStatisticDTO();
        dto.setId(fraudLabelStatistic.getId());
        
       dto.setTemplateCount(fraudLabelStatistic.getTemplateCount());
        dto.setFraudLabelName(fraudLabelStatistic.getFraudLabel().getName());
        return dto;
    }
}
