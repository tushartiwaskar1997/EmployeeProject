package com.example.employee_app.controller;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.DepartmentRequestDto;
import com.example.employee_app.dto.HandleRequest;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.service.DepartmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/dept")
public class DepartmentController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DepartmentService dptservice;

    @GetMapping("/deptlist")
    public ResponseEntity<Object> GetTheListofDepartments() {
        return HandleRequest.createResponse(
                MessageConfig.OPERATION_DONE_SUCCESSFULLY,
                HttpStatus.OK,
                dptservice.getthedepartmentlist());
    }

    @GetMapping("/deptbyid")
    public ResponseEntity<Object> GetTheDeptDetailsById(@RequestParam("id") String id) {
        if(ChecifIDisPresentorNot(id))
        {
            return  HandleRequest.createResponse(MessageConfig.ID_IS_MISSING,HttpStatus.NOT_FOUND,null);
        }
        Optional<DepartmentDetails> deptoptional = dptservice.getthedepartmentbyid(Long.parseLong(id));
        if (deptoptional.isPresent()) {
            DepartmentDetails deptObj = deptoptional.get();
            return HandleRequest.createResponse(
                    MessageConfig.OPERATION_DONE_SUCCESSFULLY,
                    HttpStatus.OK,
                    deptObj);
        }
        return HandleRequest.createResponse(
                MessageConfig.DEPARTMENT_NOT_FOUND,
                HttpStatus.NOT_FOUND, null);
    }

    @DeleteMapping("/deptdelete")
    public ResponseEntity<Object> DeleteDeptByID(@RequestParam("id") String id) {
        if(ChecifIDisPresentorNot(id))
        {
            return  HandleRequest.createResponse(MessageConfig.ID_IS_MISSING,HttpStatus.NOT_FOUND,null);
        }
        String response = dptservice.deletethedepartmentbyid(Long.parseLong(id));
        return HandleRequest.createResponse(response, HttpStatus.OK, null);
    }

    @PostMapping("/saveDept")
    public ResponseEntity<Object> SaveTheDeptDetails(@RequestParam("departmentdata") String Deptdetails) throws JsonProcessingException {
        DepartmentRequestDto deptdto = objectMapper.readValue(Deptdetails, DepartmentRequestDto.class);
        String checkvalidation = CheckValidation(deptdto);
        if (checkvalidation == null) {
            DepartmentDetails dept = new DepartmentDetails();
            dept.setDepartmentName(deptdto.getDepartmentName().toLowerCase());
            dept.setIsActive(deptdto.getIsActive());
            dept.setCreatedDate(LocalDateTime.now());
            dept.setCreatedBy("User");
            dept.setTotalEmployee(0L);
            return HandleRequest.createResponse(MessageConfig.DEPARTMENT_ADDED_SUCCESSFULLY, HttpStatus.CREATED, dptservice.savethedepartmentdetails(dept));
        }
        return HandleRequest.createResponse(MessageConfig.OPERATION_FAIL, HttpStatus.NOT_ACCEPTABLE, checkvalidation);
    }

    @PutMapping("/updateDept")
    public ResponseEntity<Object> UpdateTheDepartmentDetails(@RequestParam("id") String id, @RequestParam("departmentdata") String Deptdetails) throws JsonProcessingException {
        if(ChecifIDisPresentorNot(id))
        {
            return  HandleRequest.createResponse(MessageConfig.ID_IS_MISSING,HttpStatus.NOT_FOUND,null);
        }
        DepartmentRequestDto deptdto = objectMapper.readValue(Deptdetails, DepartmentRequestDto.class);
        Optional<DepartmentDetails> deptoptional = dptservice.getthedepartmentbyid(Long.parseLong(id));
        if (deptoptional.isPresent()) {
            DepartmentDetails dept = deptoptional.get();
            if (!dept.getDepartmentName().equals(deptdto.getDepartmentName().toLowerCase())) {
                Optional<DepartmentDetails> checkifDepartmentpresent = dptservice.chekifDepartmentispresent(deptdto.getDepartmentName().toLowerCase());
                if (checkifDepartmentpresent.isPresent()) {
                    return HandleRequest.createResponse(MessageConfig.DEPARTMENT_ALREADY_EXIST, HttpStatus.NOT_ACCEPTABLE, null);
                } else {
                    dept.setDepartmentName(deptdto.getDepartmentName().toLowerCase());
                }
            }
            dept.setIsActive(deptdto.getIsActive());
            dept.setUpdatedDate(LocalDateTime.now());
            dept.setUpdatedBy("Admin");
            return HandleRequest.createResponse(MessageConfig.DEPARTMENT_UPDATED_SUCCESSFULLY, HttpStatus.OK, dptservice.savethedepartmentdetails(dept));
        }
        return HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
    }

    public String CheckValidation(DepartmentRequestDto deptdto) {
        if (deptdto.getDepartmentName() == null || deptdto.getDepartmentName().isEmpty() || deptdto.getDepartmentName().length() == 0) {
            return MessageConfig.DEPARTMENT_NAME_MISSING;
        }
        if (deptdto.getIsActive().describeConstable().isEmpty() || deptdto.getIsActive() == null) {
            return MessageConfig.DEPARTMENT_STATUS_NOT_PROPER;
        }
        Optional<DepartmentDetails> ChecKIfDepartmentNameISPresentOrNot = dptservice.chekifDepartmentispresent(deptdto.getDepartmentName());
        if (ChecKIfDepartmentNameISPresentOrNot.isPresent()) {
            return MessageConfig.DEPARTMENT_ALREADY_EXIST;
        }
        return null;
    }
    public Boolean ChecifIDisPresentorNot(String id){
        if(id==null || id.isEmpty() ){
            return true;
        }
        return  false;
    }
}
