package com.example.employee_app.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="department")
@Data
public class DepartmentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id ;
    private String departmentName ;
    private Boolean isActive ;
    private LocalDateTime createdDate ;
    private LocalDateTime updatedDate ;
    private String createdBy ;
    private String updatedBy ;
    private Long  totalEmployee ;
}
