package com.example.employee_app.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name= "employees")
@Data
public class EmployeeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String  empName ;
    private LocalDate dbo ;
    private String email;
    private Boolean isActive ;
    private Integer createdBy ;
    private Integer updatedBy ;
    private LocalDateTime createdDate ;
    private LocalDateTime updatedDate ;



    @ManyToOne
    @JoinColumn(name= "desigantion_id")
    private DesignationDetails designationDetails ;
    @ManyToOne
    @JoinColumn(name= "department_id")
    private DepartmentDetails departmentDetails ;
}
