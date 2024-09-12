package com.example.employee_app.service;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.repository.DesignationRepository;
import com.example.employee_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DesignationService {

    @Autowired
    private DesignationRepository designationRepository ;
    @Autowired
    private EmployeeRepository  employeeRepository ;

    public List<DesignationDetails> getthelistofDesignation(){
        return designationRepository.findAll();
    }

    public Optional<DesignationDetails>  getthedesignationdetailsbyid(Long id){
        return   designationRepository.findById(id);
    }
    public  String Deletethedesignationbyid(Long id){
        Optional<DesignationDetails> designationOptional = getthedesignationdetailsbyid(id);
        if(designationOptional.isPresent()){
            DesignationDetails designationDetails = designationOptional.get();
            if(designationDetails.getTotalEmployee()==0)
            {
                designationDetails.setIsActive(false);
                designationRepository.save(designationDetails);
                return MessageConfig.DESIGNATION_DELETED_SUCCESSFULLY;
            }else
            {
                return  MessageConfig.DESIGNATION_CANNOT_BE_DELETED;
            }
        }
        return MessageConfig.DESIGNATION_NOT_FOUND;
    }
    public DesignationDetails savethedesignation(DesignationDetails designation){
        return  designationRepository.save(designation);
    }
    public  boolean  CheckifDesignationisRefferedtoAnyEmployee(Long id){
        return    employeeRepository.existsByDesignationDetails_DesignationId(id);
    }
    public List<DesignationDetails> GetTheListOfDesigantionAsperTheDeptid(Long id){
        return designationRepository.findByDepartmentId(id);
    }
    public Optional<DesignationDetails> CheckIfDesignatioNmaeExistorNot(String name){
        return designationRepository.findByDesignationName(name);
    }
}
