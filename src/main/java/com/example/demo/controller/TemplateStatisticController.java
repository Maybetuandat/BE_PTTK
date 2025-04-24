package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.TemplateStatistic;
import com.example.demo.service.TemplateStatisticService;

@RestController
@RequestMapping("/api/fraud-template-statistic")
public class TemplateStatisticController {


    @Autowired
    private TemplateStatisticService templateStatisticService;



    @GetMapping
    public ResponseEntity<TemplateStatistic> getAll() {
        return ResponseEntity.ok(templateStatisticService.createGlobalStatistic());
    }


    
}
