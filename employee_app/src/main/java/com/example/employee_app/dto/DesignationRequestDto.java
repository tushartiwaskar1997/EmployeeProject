package com.example.employee_app.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DesignationRequestDto {

    private  String deptID ;
    private  String designationName ;
    private  Boolean is_Active_status ;

}
