package com.example.employee_app.controller;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.EmployeeRequestDto;
import com.example.employee_app.dto.HandleRequest;
import com.example.employee_app.email.EmailSchedularBirthDay;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.model.EmployeeDetails;
import com.example.employee_app.schedular.EmailSchedular;
import com.example.employee_app.service.DepartmentService;
import com.example.employee_app.service.DesignationService;
import com.example.employee_app.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/emp")
public class EmployeeController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EmployeeService empService ;
    @Autowired
    private DesignationService desigService ;
    @Autowired
    private DepartmentService deptService ;
    @Autowired
    private EmailSchedularBirthDay emailSchedularBirthDay;
    @Autowired
    private EmailSchedular emailSchedular;

    //LIST-get ,BYID-get  save-post ,delete-get ,updated-put
    @GetMapping("/ListofEmp")
    public ResponseEntity<Object>  GetTheListOfEmployees(){
        return HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY , HttpStatus.OK,empService.getthelistoftheemployees());
    }
    @GetMapping("/getempbyid")
    public ResponseEntity<Object>  GetTheEmployeeByID(@RequestParam("id")String id )
    {
        Optional<EmployeeDetails> empOptional =  empService.getheemployeedetailsbyid(Integer.parseInt(id));
        if(empOptional.isPresent()){
            return  HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY,HttpStatus.OK,empOptional.get());
        }
        return HandleRequest.createResponse(MessageConfig.EMPLOYEE_NOT_FOUND,HttpStatus.BAD_REQUEST,null);
    }
    @GetMapping("/deletetheemp")
    public ResponseEntity<Object> DeleteTHeEmployeebyId(@RequestParam("id")String id ){
        String response =  empService.deletetheemployeebyid(Integer.parseInt(id));
        return HandleRequest.createResponse(response,HttpStatus.OK,null);
    }
    @PostMapping("/savetheemp")
    public ResponseEntity<Object>  SaveTheEmployee(@RequestParam("pojo") String empdetails) throws JsonProcessingException {
        EmployeeRequestDto empdto  =  objectMapper.readValue(empdetails,EmployeeRequestDto.class);
        String validataioncheck = CheckForTheValidations(empdto);
        if(validataioncheck==null){
            EmployeeDetails emp =  new EmployeeDetails();
            emp.setEmpname(empdto.getEmployee_name());
            emp.setEmail(empdto.getEmail());
            Optional<DesignationDetails> designationOptionl =  desigService.getthedesignationdetailsbyid(Integer.parseInt(empdto.getDeignationid()));
            if(designationOptionl.isPresent()){
                emp.setDesignationDetails(designationOptionl.get());
            }else
            {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND,HttpStatus.NOT_FOUND,null);
            }
            emp.setCreatedby(1);
            emp.setCreatedon(LocalDateTime.now());
            emp.setDbo(empdto.getDbo());
            emp.set_active_student(empdto.getIs_active_status());
            return  HandleRequest.createResponse(MessageConfig.EMPLOYEE_ADDED_SUCCESSFULLY,HttpStatus.CREATED,empService.Savetheemployee(emp));
        }
        return  HandleRequest.createResponse(MessageConfig.OPERATION_FAIL,HttpStatus.NOT_ACCEPTABLE,validataioncheck);
    }
    @PutMapping("/updatemep")
    public ResponseEntity<Object>  UpdateTheEmployee(@RequestParam("id")String id ,@RequestParam("pojo")String empdetails) throws JsonProcessingException {
        EmployeeRequestDto empdto =  objectMapper.readValue(empdetails,EmployeeRequestDto.class);
        Optional<EmployeeDetails> empOptional =  empService.getheemployeedetailsbyid(Integer.parseInt(id));
        if(empOptional.isPresent())
        {
            EmployeeDetails emp =  empOptional.get();
            emp.setEmpname(empdto.getEmployee_name());
            if(!emp.getEmail().equals(empdto.getEmail()))
            {
               if(empService.findtheemployeebyeemail(empdto.getEmail()).isPresent()) {
                   return HandleRequest.createResponse(MessageConfig.DUPLICATE_EMAIL_ISpRESENT, HttpStatus.NOT_ACCEPTABLE, null);
               }else{
               emp.setEmail(empdto.getEmail());
            }
            }
            Optional<DesignationDetails> designationOptionl =  desigService.getthedesignationdetailsbyid(Integer.parseInt(empdto.getDeignationid()));
            if(designationOptionl.isPresent()){
                emp.setDesignationDetails(designationOptionl.get());
            }else
            {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND,HttpStatus.NOT_FOUND,null);
            }
            emp.set_active_student(empdto.getIs_active_status());
            emp.setDbo(empdto.getDbo());
            emp.setUpdatedby(1);
            emp.setUpdatedon(LocalDateTime.now());
            return HandleRequest.createResponse(MessageConfig.EMPLOYEE_UPDATED_SUCCESSFULLY,HttpStatus.CREATED,empService.Savetheemployee(emp));
        }
        return HandleRequest.createResponse(MessageConfig.OPERATION_FAIL,HttpStatus.NOT_ACCEPTABLE,null);
    }
    @GetMapping("/email")
    public String SendCustomDateEmail(@RequestParam("month")String month , @RequestParam("day") String day) throws MessagingException {
        emailSchedular.sendtheGreetingOfBirthday_direct(Integer.parseInt(month),Integer.parseInt(day));
        return "done ";
    }


    public String CheckForTheValidations(EmployeeRequestDto employeeRequestDto){

        if(employeeRequestDto.getDeignationid()==null || employeeRequestDto.getDeignationid().isEmpty()){
            return   MessageConfig.EMP_DESIGNATIONID_MISSING;
        }
        if(employeeRequestDto.getEmployee_name()==null|| employeeRequestDto.getEmployee_name().isEmpty()){
            return MessageConfig.EMP_NAME_MISSING;
        }

        Optional<EmployeeDetails> gettheemaildetails  = empService.findtheemployeebyeemail(employeeRequestDto.getEmail());
        if(gettheemaildetails.isPresent()){
            return  MessageConfig.DUPLICATE_EMAIL_ISpRESENT;
        }
        return  null ;
    }
}
