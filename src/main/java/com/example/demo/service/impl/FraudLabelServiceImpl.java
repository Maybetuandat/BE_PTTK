package com.example.demo.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
       
         if(fraudLabel.getColor() != null) {
             newFraudLabel.setColor(fraudLabel.getColor());
         }
         return fraudLabelRepository.save(newFraudLabel);
    }

    @Override
    public void deleteFraudLabel(int id) {
        fraudLabelRepository.deleteById(id);
    }

    @Override
    public FraudLabel getFraudLabelDTO(int id) {
        return fraudLabelRepository.findById(id).orElse(null);
    }

    @Override
    public List<FraudLabel> getFraudLabelsDTO() {
        List<FraudLabel> listFraudLabels =  fraudLabelRepository.findAll();


        
        return listFraudLabels;

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
