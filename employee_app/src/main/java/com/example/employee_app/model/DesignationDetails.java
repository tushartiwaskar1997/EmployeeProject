package com.example.employee_app.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name ="designation")
@Data
public class DesignationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  designationid ;

    @ManyToOne
    @JoinColumn(name = "departmentid")
    private DepartmentDetails  departmentDetails ;

    private String  designationname ;
    private int  createdby ;
    private int  updatedby ;
    private LocalDateTime createdon ;
    private LocalDateTime updatedon ;
    private boolean is_active_status ;
}
