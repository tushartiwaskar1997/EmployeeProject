package com.example.employee_app.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DesignationRequestDto {
    private  String designationId ;
    private  String departmentId ;
    private  String designationName ;
    private  Boolean isActive ;

}
