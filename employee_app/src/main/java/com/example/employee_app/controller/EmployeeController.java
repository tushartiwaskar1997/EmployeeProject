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
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
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

    @GetMapping("/ListofEmp")
    public ResponseEntity<Object>  GetTheListOfEmployees(){
        return HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY , HttpStatus.OK,empService.getthelistoftheemployees());
    }
    @GetMapping("/getempbyid")
    public ResponseEntity<Object>  GetTheEmployeeByID(@RequestParam("id")String id )
    {
        if(ChecifIDisPresentorNot(id))
        {
            return  HandleRequest.createResponse(MessageConfig.ID_IS_MISSING,HttpStatus.NOT_FOUND,null);
        }
        Optional<EmployeeDetails> empOptional =  empService.getheemployeedetailsbyid(Long.parseLong(id));
        if(empOptional.isPresent()){
            return  HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY,HttpStatus.OK,empOptional.get());
        }
        return HandleRequest.createResponse(MessageConfig.EMPLOYEE_NOT_FOUND,HttpStatus.BAD_REQUEST,null);
    }
    @DeleteMapping("/deletetheemp")
    public ResponseEntity<Object> DeleteTHeEmployeebyId(@RequestParam("id")String id ){
        if(ChecifIDisPresentorNot(id))
        {
            return  HandleRequest.createResponse(MessageConfig.ID_IS_MISSING,HttpStatus.NOT_FOUND,null);
        }
        EmployeeDetails emp =  empService.deletetheemployeebyid(Long.parseLong(id));
        if(emp!=null){
            return  HandleRequest.createResponse(MessageConfig.EMPLOYEE_DELETED_SUCCESSFULLY,HttpStatus.OK,emp);
        }
        return HandleRequest.createResponse(MessageConfig.EMPLOYEE_NOT_FOUND,HttpStatus.OK,null);
    }
    @PostMapping("/savetheemp")
    public ResponseEntity<Object>  SaveTheEmployee(@RequestParam("employeedata") String empdetails, @RequestParam("image")MultipartFile imagefile) throws JsonProcessingException, IOException {
        EmployeeRequestDto empdto  =  objectMapper.readValue(empdetails,EmployeeRequestDto.class);
        String validataioncheck = CheckForTheValidations(empdto);
        if(validataioncheck==null){
            EmployeeDetails emp =  new EmployeeDetails();
            emp.setEmpName(empdto.getEmployeeName());
            emp.setEmail(empdto.getEmail());
            Optional<DesignationDetails> designationOptionl =  desigService.getthedesignationdetailsbyid(Long.parseLong(empdto.getDeignationId()));
            if(designationOptionl.isPresent()){
                emp.setDesignationDetails(designationOptionl.get());
            }else
            {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND,HttpStatus.NOT_FOUND,null);
            }
            Optional<DepartmentDetails> deptOptional =  deptService.getthedepartmentbyid(Long.parseLong(empdto.getDepartmentID()));
            if(deptOptional.isPresent()){
                emp.setDepartmentDetails(deptOptional.get());
            }else {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND,HttpStatus.NOT_FOUND,null);
            }

            emp.setCreatedBy("user");
            emp.setCreatedDate(LocalDateTime.now());
            emp.setDbo(empdto.getDbo());
            emp.setIsActive(empdto.getIsActive());
            empService.UpdateTheTotalEmployeeCount_forDeptAndDesig(emp.getDepartmentDetails().getId(),emp.getDesignationDetails().getDesignationId());
            return  HandleRequest.createResponse(MessageConfig.EMPLOYEE_ADDED_SUCCESSFULLY,HttpStatus.CREATED,empService.SavetheemployeeAndImage(emp,imagefile));
        }
        return  HandleRequest.createResponse(MessageConfig.OPERATION_FAIL,HttpStatus.NOT_ACCEPTABLE,validataioncheck);
    }
    @PutMapping("/updatemep")
    public ResponseEntity<Object>  UpdateTheEmployee(@RequestParam("id")String id ,@RequestParam("employeedata")String empdetails) throws JsonProcessingException {
        if(ChecifIDisPresentorNot(id))
        {
            return  HandleRequest.createResponse(MessageConfig.ID_IS_MISSING,HttpStatus.NOT_FOUND,null);
        }
        EmployeeRequestDto empdto =  objectMapper.readValue(empdetails,EmployeeRequestDto.class);
        Optional<EmployeeDetails> empOptional =  empService.getheemployeedetailsbyid(Long.parseLong(id));
        if(empOptional.isPresent())
        {
            EmployeeDetails emp =  empOptional.get();
            emp.setEmpName(empdto.getEmployeeName());
            if(!emp.getEmail().equals(empdto.getEmail()))
            {
               if(empService.findtheemployeebyeemail(empdto.getEmail()).isPresent()) {
                   return HandleRequest.createResponse(MessageConfig.DUPLICATE_EMAIL_IS_PRESENT, HttpStatus.NOT_ACCEPTABLE, null);
               }else{
               emp.setEmail(empdto.getEmail());
            }
            }
            Optional<DesignationDetails> designationOptionl =  desigService.getthedesignationdetailsbyid(Long.parseLong(empdto.getDeignationId()));
            if(designationOptionl.isPresent()){
                emp.setDesignationDetails(designationOptionl.get());
            }else
            {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND,HttpStatus.NOT_FOUND,null);
            }
            Optional<DepartmentDetails> departmentOptional =  deptService.getthedepartmentbyid(Long.parseLong(empdto.getDepartmentID()));
            if(departmentOptional.isPresent()){
                emp.setDepartmentDetails(departmentOptional.get());
            }else
            {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND,HttpStatus.NOT_FOUND,null);
            }
            emp.setIsActive(emp.getIsActive());
            emp.setDbo(empdto.getDbo());
            emp.setUpdatedBy("Admin");
            emp.setUpdatedDate(LocalDateTime.now());
            return HandleRequest.createResponse(MessageConfig.EMPLOYEE_UPDATED_SUCCESSFULLY,HttpStatus.CREATED,empService.Savetheemployee(emp));
        }
        return HandleRequest.createResponse(MessageConfig.OPERATION_FAIL,HttpStatus.NOT_ACCEPTABLE,null);
    }
    @GetMapping("/email")
    public String SendCustomDateEmail(@RequestParam("month")String month , @RequestParam("day") String day) throws MessagingException {
        emailSchedular.sendtheGreetingOfBirthday_direct(Integer.parseInt(month),Integer.parseInt(day));
        return "done ";
    }
    @GetMapping("/empStatusChange")
    public  ResponseEntity<Object>  SetTheEmployeeStatusToTrue(@RequestParam("id")String id){
        if(ChecifIDisPresentorNot(id))
        {
            return  HandleRequest.createResponse(MessageConfig.ID_IS_MISSING,HttpStatus.NOT_FOUND,null);
        }
        Optional<EmployeeDetails>  employeeDetailsOptional =  empService.getheemployeedetailsbyid(Long.parseLong(id));
        if(employeeDetailsOptional.isPresent()){
            EmployeeDetails empObj =  employeeDetailsOptional.get();
            if(empObj.getDesignationDetails().getIsActive()==false){
                DepartmentDetails dept =  empObj.getDepartmentDetails();
                dept.setIsActive(true);
                deptService.savethedepartmentdetails(dept);
            }if(empObj.getDesignationDetails().getIsActive()==false){
                DesignationDetails desig = empObj.getDesignationDetails();
                desig.setIsActive(true);
                desigService.savethedesignation(desig);
            }
            empService.UpdateTheTotalEmployeeCount_forDeptAndDesig(empObj.getDepartmentDetails().getId(),empObj.getDesignationDetails().getDesignationId());
            empObj.setIsActive(true);
            empObj.setUpdatedBy("Admin1");
            empObj.setUpdatedDate(LocalDateTime.now());
            return HandleRequest.createResponse(MessageConfig.EMPLOYEE_STATUS_CHANGE_TO_ACTIVE,HttpStatus.OK,empService.Savetheemployee(empObj));
        }
        return HandleRequest.createResponse(MessageConfig.OPERATION_FAIL,HttpStatus.NOT_FOUND,MessageConfig.EMPLOYEE_NOT_FOUND);
    };


    public String CheckForTheValidations(EmployeeRequestDto employeeRequestDto){

        if(employeeRequestDto.getDeignationId()==null || employeeRequestDto.getDeignationId().isEmpty()){
            return   MessageConfig.EMP_DESIGNATION_ID_MISSING;
        }
        if(employeeRequestDto.getDepartmentID()==null ||  employeeRequestDto.getDepartmentID().isEmpty()){
            return MessageConfig.DEPARTMENT_NOT_FOUND;
        }
        if(employeeRequestDto.getEmployeeName()==null|| employeeRequestDto.getEmployeeName().isEmpty()){
            return MessageConfig.EMP_NAME_MISSING;
        }

        Optional<EmployeeDetails> gettheemaildetails  = empService.findtheemployeebyeemail(employeeRequestDto.getEmail());
        if(gettheemaildetails.isPresent()){
            return  MessageConfig.DUPLICATE_EMAIL_IS_PRESENT;
        }
        if(empService.CheckIfEmployeeOfSameNamePresentORNot(employeeRequestDto.getEmployeeName())){
            return MessageConfig.DUPLICATE_EMPLOYEE_NAME;
        }
        return  null ;
    }
    public Boolean ChecifIDisPresentorNot(String id){
        if(id==null || id.isEmpty() ){
            return true;
        }
        return  false;
    }
}
