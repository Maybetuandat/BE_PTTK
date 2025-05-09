package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.FraudLabel;
import com.example.demo.model.FraudTemplate;

@Repository
public interface FraudTemplateRepository  extends JpaRepository<FraudTemplate, Integer> {

   

 
}
