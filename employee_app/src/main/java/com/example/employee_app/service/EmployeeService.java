package com.example.employee_app.service;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.model.EmployeeDetails;
import com.example.employee_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository ;

    public List<EmployeeDetails> getthelistoftheemployees(){
        return employeeRepository.findAll();
    }
    public Optional<EmployeeDetails>  getheemployeedetailsbyid(int id){
        return  employeeRepository.findById(id);
    }

    public  String  deletetheemployeebyid(int id )
    {
        if(getheemployeedetailsbyid(id).isPresent()){
            employeeRepository.deleteById(id);
            return MessageConfig.EMPLOYEE_DELETED_SUCCESSFULLY;
        }
        return MessageConfig.EMPLOYEE_NOT_FOUND;
    }
    public EmployeeDetails Savetheemployee(EmployeeDetails employeeDetails){
        return  employeeRepository.save(employeeDetails);
    }
    public Optional<EmployeeDetails>  findtheemployeebyeemail(String email){
        return  employeeRepository.findByEmail(email);
    }

    public List<EmployeeDetails> findthelistofEmployeesasperthemonthanddate(int month ,int  day){
        return  employeeRepository.findEmployeesWithBirthdayToday(month,day);
    }
}
