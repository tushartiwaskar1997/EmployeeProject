package com.example.employee_app.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DepartmentRequestDto {
    private  String DepartmentName ;
    private  Boolean  isActive ;

}
