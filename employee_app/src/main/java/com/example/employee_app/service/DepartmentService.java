package com.example.employee_app.service;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.model.EmployeeDetails;
import com.example.employee_app.repository.DepartmentRepository;
import com.example.employee_app.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;


    public List<DepartmentDetails> getthedepartmentlist(){
        return   departmentRepository.findAll();
    }
    public Optional<DepartmentDetails>  getthedepartmentbyid(int id){
        return  departmentRepository.findById(id);
    }

    public String deletethedepartmentbyid(int  id )
    {
        if(getthedepartmentbyid(id).isPresent()){
            if(CheckIfanyDesignationExistForGivenDepartment(id)){
                return  MessageConfig.DEPARTMENT_CANNOTbE_DELERTED;
            }else {
                departmentRepository.deleteById(id);
                return MessageConfig.DEPARTMENT_DELETED_SUCCESSFULLY ;
            }
        }
        return  MessageConfig.DEPARTMENT_NOT_FOUND ;
    }

    public DepartmentDetails savethedepartmentdetails(DepartmentDetails dept){
        return departmentRepository.save(dept);
    }

    public Optional<DepartmentDetails>  chekifDepartmentispresent(String depname){
        return  departmentRepository.findBydepartmentname(depname);
    }

    public boolean  CheckIfanyDesignationExistForGivenDepartment(int id){
        boolean hasDesignation =  designationRepository.existsByDepartmentDetails_Id(id);
        return  hasDesignation;
    }
}
