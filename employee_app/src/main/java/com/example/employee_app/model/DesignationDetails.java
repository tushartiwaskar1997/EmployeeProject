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
    private Long  designationId ;
    private String  designationName ;
    private Boolean isActive ;
    private Integer  createdBy ;
    private Integer  updatedBy ;
    private LocalDateTime createdDate ;
    private LocalDateTime updatedDate ;


    @ManyToOne
    @JoinColumn(name = "departmentid", referencedColumnName ="id")
    private DepartmentDetails  departmentDetails ;

}
