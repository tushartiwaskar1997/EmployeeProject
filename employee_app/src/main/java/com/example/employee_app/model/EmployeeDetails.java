package com.example.employee_app.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
//at response object were not comming at
@JsonPropertyOrder ({"id", "empName", "dbo", "email", "isActive", "createdBy", "updatedBy", "createdDate", "updatedDate", "designationDetails", "departmentDetails"})
@Entity
@Table(name= "employees")
@Data
public class EmployeeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  empName ;
    private LocalDate dbo ;
    private String email;
    private Boolean isActive ;
    private String createdBy ;
    private String updatedBy ;
    private LocalDateTime createdDate ;
    private LocalDateTime updatedDate ;

    @Lob
    private byte[] imageData ;

    @ManyToOne
    @JoinColumn(name= "desigantion_id")
    private DesignationDetails designationDetails ;
    @ManyToOne
    @JoinColumn(name= "department_id")
    private DepartmentDetails departmentDetails ;
}
