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
    private int empid;
    private String  empname ;
    private LocalDate dbo ;
    private String email;
    @ManyToOne
    @JoinColumn(name= "desigantion_id")
    private DesignationDetails designationDetails ;
//    @ManyToOne
//    @JoinColumn(name= "department_id")
//    private DepartmentDetails departmentDetails ;
    private int createdby ;
    private int updatedby ;
    private LocalDateTime createdon ;
    private LocalDateTime updatedon ;
    private boolean is_active_student ;
}
