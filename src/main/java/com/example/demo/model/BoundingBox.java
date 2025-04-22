package com.example.demo.model;

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
    private int id;
    
    // Coordinates normalized from 0-1 (YOLO format)
    private float xCenter; // Tọa độ x của tâm hộp, chuẩn hóa từ 0-1
    private float yCenter; // Tọa độ y của tâm hộp, chuẩn hóa từ 0-1
    private float width;   // Chiều rộng của hộp, chuẩn hóa từ 0-1
    private float height;  // Chiều cao của hộp, chuẩn hóa từ 0-1
    
    // Lưu tọa độ gốc (pixel) cho việc hiển thị
    private int xPixel;
    private int yPixel; 
    private int widthPixel;
    private int heightPixel;
    
    private float confidence;
    
    @ManyToOne
    @JoinColumn(name = "fraud_template_id", nullable = false)
    private FraudTemplate fraudTemplate;
    
    // Chuyển đổi tọa độ pixel sang tọa độ chuẩn hóa (0-1) cho YOLO
    public void normalizeCoordinates() {
        int imgWidth = this.fraudTemplate.getWidth();
        int imgHeight = this.fraudTemplate.getHeight();
        
        this.xCenter = (float)(this.xPixel + this.widthPixel/2.0) / imgWidth;
        this.yCenter = (float)(this.yPixel + this.heightPixel/2.0) / imgHeight;
        this.width = (float)this.widthPixel / imgWidth;
        this.height = (float)this.heightPixel / imgHeight;
    }
    
    // Chuyển từ tọa độ chuẩn hóa sang pixel
    public void denormalizeCoordinates() {
        int imgWidth = this.fraudTemplate.getWidth();
        int imgHeight = this.fraudTemplate.getHeight();
        
        this.widthPixel = (int)(this.width * imgWidth);
        this.heightPixel = (int)(this.height * imgHeight);
        this.xPixel = (int)(this.xCenter * imgWidth - this.widthPixel/2.0);
        this.yPixel = (int)(this.yCenter * imgHeight - this.heightPixel/2.0);
    }
}