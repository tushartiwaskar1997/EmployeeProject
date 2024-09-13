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
    private String employeeId ;
    private String employeeName;
    private String departmentId ;
    private String deignationId;
    private String email;
    private LocalDate dbo ;
}
