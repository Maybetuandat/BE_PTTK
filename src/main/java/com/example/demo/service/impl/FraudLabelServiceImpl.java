package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.FraudLabelDTO;
import com.example.demo.dto.mapper.FraudLabelMapper;
import com.example.demo.model.FraudLabel;
import com.example.demo.repository.FraudLabelRepository;
import com.example.demo.service.FraudLabelService;

@Service
public class FraudLabelServiceImpl  implements FraudLabelService {

    @Autowired
    private FraudLabelRepository fraudLabelRepository;
    @Override
    public FraudLabel addFraudLabel(FraudLabel fraudLabel) {
        return fraudLabelRepository.save(fraudLabel);
    }

    @Override
    public FraudLabel updateFraudLabel(FraudLabel fraudLabel) {
         FraudLabel newFraudLabel = fraudLabelRepository.findById(fraudLabel.getId()).orElse(null);
         if(fraudLabel.getName() != null) {
             newFraudLabel.setName(fraudLabel.getName());
         }
         if(fraudLabel.getDescription() != null) {
             newFraudLabel.setDescription(fraudLabel.getDescription());
         }
         return fraudLabelRepository.save(newFraudLabel);
    }

    @Override
    public void deleteFraudLabel(int id) {
        fraudLabelRepository.deleteById(id);
    }

    @Override
    public FraudLabelDTO getFraudLabelDTO(int id) {
        return FraudLabelMapper.mapToDTO(fraudLabelRepository.findById(id).orElse(null));
    }

    @Override
    public List<FraudLabelDTO> getFraudLabelsDTO() {
        List<FraudLabel> listFraudLabels =  fraudLabelRepository.findAll();


        List<FraudLabelDTO> listFraudLabelsDTO = new ArrayList<>();
        for(FraudLabel fraudLabel : listFraudLabels) {
            listFraudLabelsDTO.add(FraudLabelMapper.mapToDTO(fraudLabel));
        }
        return listFraudLabelsDTO;

    }

    @Override
    public FraudLabel getFraudLabelById(int id) {
          return fraudLabelRepository.findById(id).orElse(null);
    }

    @Override
    public List<FraudLabel> getFraudLabels() {
        return fraudLabelRepository.findAll();
    }

}
