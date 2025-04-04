package com.example.demo.dto.mapper;

import com.example.demo.dto.FraudLabelDTO;
import com.example.demo.model.FraudLabel;

public class FraudLabelMapper {
    public static FraudLabelDTO mapToDTO (FraudLabel fraudLabel)
    {
           FraudLabelDTO fraudLabelDTO = new FraudLabelDTO();
           fraudLabelDTO.setId(fraudLabel.getId());
           fraudLabelDTO.setName(fraudLabel.getName());
           fraudLabelDTO.setDescription(fraudLabel.getDescription());
           return fraudLabelDTO;
    }
    public static FraudLabel mapToModel (FraudLabelDTO fraudLabelDTO)
    {
           FraudLabel fraudLabel = new FraudLabel();
           fraudLabel.setId(fraudLabelDTO.getId());
           fraudLabel.setName(fraudLabelDTO.getName());
           fraudLabel.setDescription(fraudLabelDTO.getDescription());
           return fraudLabel;
    }
}
