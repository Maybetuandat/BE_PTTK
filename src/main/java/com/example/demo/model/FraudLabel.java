package com.example.demo.model;



import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString

public class FraudLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer classId;
    private String name;
    

   @Column(nullable = false, updatable = false)
    private String createAt;

    private String color;
    
    @JsonIgnore
    @OneToMany(mappedBy = "fraudLabel", cascade = CascadeType.ALL)
    private List<BoundingBox> boundingBoxes;

    @PrePersist
    protected void onCreate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.createAt = formatter.format(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));
      
    }
}   
