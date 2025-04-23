package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoundingBox {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    
    // toa do cua bounding box trong yolo co the hieu duoc 
    private float xCenter; 
    private float yCenter; 
    private float width;   
    private float height;  
    
    //toa do pixel cua anh de co the hien thi len 
    private Integer xPixel;
    private Integer yPixel; 
    private Integer widthPixel;
    private Integer heightPixel;
    
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "fraud_template_id", nullable = false)
    private FraudTemplate fraudTemplate;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "fraud_label_id", nullable = false)
    private FraudLabel fraudLabel;
    
    public void convertToYoloParameter() {
        int imgWidth = this.fraudTemplate.getWidth();
        int imgHeight = this.fraudTemplate.getHeight();
        
        this.xCenter = (float)(this.xPixel + this.widthPixel/2.0) / imgWidth;
        this.yCenter = (float)(this.yPixel + this.heightPixel/2.0) / imgHeight;
        this.width = (float)this.widthPixel / imgWidth;
        this.height = (float)this.heightPixel / imgHeight;
    }
    
    
    public void convertToPixel() {
        int imgWidth = this.fraudTemplate.getWidth();
        int imgHeight = this.fraudTemplate.getHeight();
        
        this.widthPixel = (int)(this.width * imgWidth);
        this.heightPixel = (int)(this.height * imgHeight);
        this.xPixel = (int)(this.xCenter * imgWidth - this.widthPixel/2.0);
        this.yPixel = (int)(this.yCenter * imgHeight - this.heightPixel/2.0);
    }
}