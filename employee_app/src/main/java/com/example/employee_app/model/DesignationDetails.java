package com.example.employee_app.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name ="designation")
@Data
public class DesignationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id ;
    private String  designationName ;
    private Boolean isActive ;
    private String  createdBy ;
    private String  updatedBy ;
    private LocalDateTime createdDate ;
    private LocalDateTime updatedDate ;
    private Long totalEmployee ;

}
