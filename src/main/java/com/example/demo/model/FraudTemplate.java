package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FraudTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    

    private String name;
    @ManyToOne
    @JoinColumn(name = "fraud_label_id", nullable = false)
    private FraudLabel fraudLabel;
    private String imageUrl;
    private Integer height;
    private Integer width;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
    }
    
}
