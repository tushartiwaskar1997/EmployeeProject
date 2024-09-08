package com.example.employee_app.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name ="department")
@Data
public class DepartmentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id ;
    private String departmentname ;
    private LocalDateTime createdon ;
    private LocalDateTime updatedon ;
    private int createdby ;
    private int updatedby ;
    private boolean is_active ;
}
