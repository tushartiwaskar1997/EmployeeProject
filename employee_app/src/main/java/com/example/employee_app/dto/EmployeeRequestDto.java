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
    private Long employeeId;
    private String employeeName;
    private Long departmentId;
    private Long designationId;
    private String email;
    private LocalDate dbo;
}
