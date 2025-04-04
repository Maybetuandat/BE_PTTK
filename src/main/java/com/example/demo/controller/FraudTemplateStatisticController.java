package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FraudTemplateDTO;
import com.example.demo.dto.FraudTemplateStatisticDTO;
import com.example.demo.service.FraudTemplateStatisticService;

@RestController
@RequestMapping("/api/statistics/fraud-template")
public class FraudTemplateStatisticController {

    @Autowired
    FraudTemplateStatisticService fraudTemplateStatisticsService;

    @GetMapping()
    public ResponseEntity<List<FraudTemplateStatisticDTO>> getAllFraudTemplateStatistics() {
      //  fraudTemplateStatisticsService.createFraudLabelStatistics();
        List<FraudTemplateStatisticDTO> fraudTemplateStatistics = fraudTemplateStatisticsService.getAllStatisticsDTO();
        return ResponseEntity.ok(fraudTemplateStatistics);
    }
}
