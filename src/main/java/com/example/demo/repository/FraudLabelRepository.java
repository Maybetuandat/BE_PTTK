package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.FraudLabel;

@Repository
public interface FraudLabelRepository  extends JpaRepository<FraudLabel, Integer> {

}
