package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.BoundingBox;
import com.example.demo.model.FraudLabel;
import com.example.demo.model.FraudTemplate;
import com.example.demo.model.TemplateCount;
import com.example.demo.model.TemplateStatistic;
import com.example.demo.repository.FraudLabelRepository;
import com.example.demo.repository.FraudTemplateRepository;
import com.example.demo.service.TemplateStatisticService;

@Service
public class TemplateStatisticServiceImpl implements TemplateStatisticService {

    @Autowired
    private FraudTemplateRepository fraudTemplateRepository;
    
    @Autowired
    private FraudLabelRepository fraudLabelRepository;
    
   
    public TemplateStatistic createGlobalStatistic() {

        List<FraudTemplate> allTemplates = fraudTemplateRepository.findAll();

        int totalCount = allTemplates.size();
        int unlabeledCount = 0;
        int labeledCount = 0;

        Map<Integer, Integer> labelCountMap = new HashMap<>();
        Map<Integer, String> labelNameMap = new HashMap<>();

    
        List<FraudLabel> allLabels = fraudLabelRepository.findAll();
        for (int i = 0; i < allLabels.size(); i++) {
            FraudLabel label = allLabels.get(i);
            labelNameMap.put(label.getId(), label.getName() + " " + label.getColor());
        }

   
        for (int i = 0; i < allTemplates.size(); i++) {
            FraudTemplate template = allTemplates.get(i);
            List<BoundingBox> boundingBoxes = template.getBoundingBoxes();

            if (boundingBoxes == null || boundingBoxes.isEmpty()) {
                unlabeledCount++;
                continue;
            }

            labeledCount++;

      
            Set<Integer> uniqueLabelIds = new HashSet<>();
            for (int j = 0; j < boundingBoxes.size(); j++) {
                BoundingBox box = boundingBoxes.get(j);
                if (box.getFraudLabel() != null) {
                    uniqueLabelIds.add(box.getFraudLabel().getId());
                }
            }

        
            for (Integer labelId : uniqueLabelIds) {
                if (labelCountMap.containsKey(labelId)) {
                    labelCountMap.put(labelId, labelCountMap.get(labelId) + 1);
                } else {
                    labelCountMap.put(labelId, 1);
                }
            }
        }

    
        List<TemplateCount> templateCounts = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : labelCountMap.entrySet()) {
            
            Integer count = entry.getValue();
            String labelNameAndColor = labelNameMap.containsKey(entry.getKey()) ? labelNameMap.get(entry.getKey()) : "Unknown Label";
            String[] parts = labelNameAndColor.split(" ");
            String labelName = parts[0];
            String color = parts.length > 1 ? parts[1] : "Unknown Color";

        TemplateCount templateCount = new  TemplateCount(labelName, count, color);

        templateCounts.add(templateCount);
    }

    
        for (int i = 0; i < templateCounts.size() - 1; i++) {
            for (int j = i + 1; j < templateCounts.size(); j++) {
                if (templateCounts.get(i).getCount() < templateCounts.get(j).getCount()) {
                    TemplateCount temp = templateCounts.get(i);
                    templateCounts.set(i, templateCounts.get(j));
                    templateCounts.set(j, temp);
                }
            }
        }

    
        return TemplateStatistic.builder()
                .totalTemplatesCount(totalCount)
                .labeledTemplatesCount(labeledCount)
                .unlabeledTemplatesCount(unlabeledCount)
                .templateCounts(templateCounts)
                .build();
}

    
   
    
   
}
    