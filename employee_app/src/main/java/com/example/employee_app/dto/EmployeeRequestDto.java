package com.example.employee_app.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Data
@Setter
@Getter
public class EmployeeRequestDto {
    private String employee_name;
    //private String deptid ;
    private String deignationid;
    private String email;
    private LocalDate dbo ;
    private Boolean is_active_status;
}
