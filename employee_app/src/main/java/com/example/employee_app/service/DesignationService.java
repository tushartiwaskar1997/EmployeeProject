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

    public Optional<DesignationDetails>  getthedesignationdetailsbyid(int id){
        return   designationRepository.findById(id);
    }
    public  String Deletethedesignationbyid(int id){
        if(getthedesignationdetailsbyid(id).isPresent()){
            if(CheckifDesignationisRefferedtoAnyEmployee(id)){
                return  MessageConfig.DESINGATION_CANNOTBE_DELETED;
            }else
            {
                designationRepository.deleteById(id);
                return MessageConfig.DESIGNATION_DELETED_SUCCESSFULLY;
            }
        }
        return MessageConfig.DESIGNATION_NOT_FOUND;
    }
    public DesignationDetails savethedesignation(DesignationDetails designation){
        return  designationRepository.save(designation);
    }
    public  boolean  CheckifDesignationisRefferedtoAnyEmployee(int id){
        boolean result =  employeeRepository.existsByDesignationDetails_Designationid(id);
        return  result;
    }
}
