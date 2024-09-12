package com.example.employee_app.service;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.model.EmployeeDetails;
import com.example.employee_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository ;
    @Autowired
    private DepartmentService departmentService ;
    @Autowired
    private DesignationService designationService;

    public List<EmployeeDetails> getthelistoftheemployees(){
        return employeeRepository.findAll();
    }
    public Optional<EmployeeDetails>  getheemployeedetailsbyid(Long id){
        return  employeeRepository.findById(id);
    }

    public  EmployeeDetails  deletetheemployeebyid(Long id )
    {
        if(getheemployeedetailsbyid(id).isPresent()){
            Optional<EmployeeDetails> Employeeoptional = employeeRepository.findById(id);
            EmployeeDetails   Employee =  Employeeoptional.get();
            Employee.setIsActive(false);

            DepartmentDetails departmentdetails  =Employee.getDepartmentDetails();
            departmentdetails.setTotalEmployee(departmentdetails.getTotalEmployee()-1L);
            departmentService.savethedepartmentdetails(departmentdetails);

            DesignationDetails designationDetails = Employee.getDesignationDetails();
            designationDetails.setTotalEmployee(designationDetails.getTotalEmployee()-1L);
            designationService.savethedesignation(designationDetails);

            return employeeRepository.save(Employee);
        }
        return null;
    }
    public EmployeeDetails SavetheemployeeAndImage(EmployeeDetails employeeDetails , MultipartFile imagefile) throws IOException {
        employeeDetails.setImageData(imagefile.getBytes());
        return  employeeRepository.save(employeeDetails);
    }
    public EmployeeDetails Savetheemployee(EmployeeDetails employeeDetails )  {
        return  employeeRepository.save(employeeDetails);
    }
    public Optional<EmployeeDetails>  findtheemployeebyeemail(String email){
        return  employeeRepository.findByEmail(email);
    }
    public List<EmployeeDetails> findthelistofEmployeesasperthemonthanddate(int month ,int  day){
        return  employeeRepository.findEmployeesWithBirthdayToday(month,day);
    }
    public Boolean CheckIfEmployeeOfSameNamePresentORNot(String employeename){
        if(employeeRepository.findByEmpName(employeename).isPresent()){
            return  true;
        }else
            return  false;
    }

    public void UpdateTheTotalEmployeeCount_forDeptAndDesig( Long Deptid ,Long DesigId){
        DepartmentDetails departmentDetails =  departmentService.getthedepartmentbyid(Deptid).get();
        departmentDetails.setTotalEmployee(departmentDetails.getTotalEmployee()+1L);
        departmentService.savethedepartmentdetails(departmentDetails);

        DesignationDetails designationDetails =  designationService.getthedesignationdetailsbyid(DesigId).get();
        designationDetails.setTotalEmployee(designationDetails.getTotalEmployee()+1L);
        designationService.savethedesignation(designationDetails);
        }
    }

