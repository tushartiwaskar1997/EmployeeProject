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

    public Optional<DepartmentDetails>  getthedepartmentbyid(Long id){
        return  departmentRepository.findById(id);
    }

    public String deletethedepartmentbyid(Long  id )
    {
        Optional<DepartmentDetails> departmentOptional  =getthedepartmentbyid(id);
        if(departmentOptional.isPresent()){
            DepartmentDetails departmentDetails = departmentOptional.get();
            if(departmentDetails.getTotalEmployee()==0){
                if(CheckIfANyDesiagnatioisStatustrue(id)){
                    return  MessageConfig.DEPARTMENT_CANNOT_BE_DELETED;
                }
                departmentDetails.setIsActive(false);
                departmentRepository.save(departmentDetails);
                return MessageConfig.DEPARTMENT_DELETED_SUCCESSFULLY ;
            }else
            {
                return  MessageConfig.DESIGNATION_CANNOT_BE_DELETED;
            }
        }
        return  MessageConfig.DEPARTMENT_NOT_FOUND ;
    }

    public DepartmentDetails savethedepartmentdetails(DepartmentDetails dept){
        return departmentRepository.save(dept);
    }

    public Optional<DepartmentDetails>  chekifDepartmentispresent(String depname){
        return  departmentRepository.findBydepartmentName(depname);
    }

    public boolean  CheckIfanyDesignationExistForGivenDepartment(Long id){
        return   designationRepository.existsByDepartmentId(id);
    }
    public Boolean CheckIfANyDesiagnatioisStatustrue(Long id){
        List<DesignationDetails> list =  designationRepository.findByDepartmentId(id);
        for (DesignationDetails desig : list){
            if(desig.getIsActive()){
                return  true;
            }
        }
        return  false;
    }
}
