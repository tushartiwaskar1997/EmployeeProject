package com.example.employee_app.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Data
@Setter
@Getter
public class DepartmentRequestDto {

    private  Long  departmentId  ;
    private  String  departmentName ;
    private  Boolean isActive ;
}
