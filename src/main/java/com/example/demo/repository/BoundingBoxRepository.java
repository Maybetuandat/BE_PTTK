package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BoundingBox;

@Repository
public interface BoundingBoxRepository  extends JpaRepository<BoundingBox, Integer>{

    List<BoundingBox> findByFraudTemplateId(Integer fraudTemplateId);
}
