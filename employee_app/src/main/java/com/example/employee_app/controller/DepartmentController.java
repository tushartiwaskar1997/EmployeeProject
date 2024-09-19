package com.example.employee_app.controller;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.DepartmentRequestDto;
import com.example.employee_app.dto.ResponseHandeler;
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
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/getDepartmentList")
    public ResponseEntity<Object> GetTheListOfDepartments() {
        return ResponseHandeler.createResponse(
                MessageConfig.OPERATION_DONE_SUCCESSFULLY,
                HttpStatus.OK,
                departmentService.GetTheDepartmentList());
    }

    @GetMapping("/getDeptById/{DepartmentId}")
    public ResponseEntity<Object> GetTheDeptDetailsById(@PathVariable("DepartmentId") Long id) {

        Optional<DepartmentDetails> deptoptional = departmentService.GetTheDepartmentById((id));
        if (deptoptional.isPresent()) {
            DepartmentDetails deptObj = deptoptional.get();
            return ResponseHandeler.createResponse(
                    MessageConfig.OPERATION_DONE_SUCCESSFULLY,
                    HttpStatus.OK,
                    deptObj);
        }
        return ResponseHandeler.createResponse(
                MessageConfig.DEPARTMENT_NOT_FOUND,
                HttpStatus.NOT_FOUND, null);
    }

    @DeleteMapping("/deleteDept/{DepartmentId}")
    public ResponseEntity<Object> DeleteDeptByID(@PathVariable("DepartmentId") Long id) {

        Optional<DepartmentDetails> departmentOptional = departmentService.GetTheDepartmentById(id);
        if (departmentOptional.isPresent()) {
            DepartmentDetails departmentDetails = departmentOptional.get();
            if(!departmentDetails.getIsActive()){
                return ResponseHandeler.createResponse(MessageConfig.DEPARTMENT_ALREADY_DELETED,HttpStatus.NOT_ACCEPTABLE,null);
            }
            if (departmentDetails.getTotalEmployee() == 0) {
                if (departmentService.CheckIfAnyDesignationIsStatusTrue(id)) {
                    return ResponseHandeler.createResponse(MessageConfig.DEPARTMENT_CANNOT_BE_DELETED,HttpStatus.NOT_ACCEPTABLE,null);
                }
                return ResponseHandeler.createResponse(departmentService.DeleteTheDepartmentById(id),HttpStatus.OK,null);
            } else {
                return ResponseHandeler.createResponse(MessageConfig.DEPARTMENT_ASSOCIATED_WITH_EMPLOYEE,HttpStatus.NOT_ACCEPTABLE,null);
            }
        }
        return ResponseHandeler.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND,HttpStatus.NOT_ACCEPTABLE,null);
    }

    @PostMapping("/saveDept")
    public ResponseEntity<Object> SaveTheDeptDetails(@RequestBody DepartmentRequestDto Deptdetails) throws JsonProcessingException {

        String checkvalidation = CheckValidation(Deptdetails);
        if (checkvalidation == null) {
            ResponseEntity<Object> response = departmentService.AddTheDepartment(Deptdetails);
            return ResponseHandeler.createResponse(MessageConfig.DEPARTMENT_ADDED_SUCCESSFULLY, response.getStatusCode(), response.getBody());
        }
        return ResponseHandeler.createResponse(checkvalidation, HttpStatus.NOT_ACCEPTABLE, null);
    }

    @PutMapping("/updateDept")
    public ResponseEntity<Object> UpdateTheDepartmentDetails(@RequestBody DepartmentRequestDto departmentDetails)  {

        String validationCheck = CheckValidationsAtUpdate(departmentDetails);
        if (validationCheck != null) {
            return ResponseHandeler.createResponse(validationCheck, HttpStatus.NOT_ACCEPTABLE, null);
        }

        Optional<DepartmentDetails> deptoptional = departmentService.GetTheDepartmentById((departmentDetails.getDepartmentId()));
        if (deptoptional.isPresent()) {

            if (!departmentDetails.getIsActive()) {
                return ResponseHandeler.createResponse(MessageConfig.DEPARTMENT_STATUS_CANNOT_SET_inACTIVE, HttpStatus.NOT_ACCEPTABLE, null);
            }

            ResponseEntity<Object> response = departmentService.UpdateTheDepartment(departmentDetails);
            if (response.getStatusCode() != HttpStatus.OK) {
                return ResponseHandeler.createResponse(response.getBody().toString(), response.getStatusCode(), null);
            } else {
                return ResponseHandeler.createResponse(MessageConfig.DEPARTMENT_UPDATED_SUCCESSFULLY, response.getStatusCode(), response.getBody());
            }
        }
        return ResponseHandeler.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
    }

    public String CheckValidation(DepartmentRequestDto deptdto) {
        if (deptdto.getDepartmentName() == null || deptdto.getDepartmentName().isEmpty() || deptdto.getDepartmentName().length() == 0) {
            return MessageConfig.DEPARTMENT_NAME_MISSING;
        }

        Optional<DepartmentDetails> ChecKIfDepartmentNameISPresentOrNot = departmentService.CheckIfDepartmentIsPresent(deptdto.getDepartmentName());
        if (ChecKIfDepartmentNameISPresentOrNot.isPresent()) {
            return MessageConfig.DEPARTMENT_ALREADY_EXIST;
        }
        return null;
    }


    public String CheckValidationsAtUpdate(DepartmentRequestDto departmentRequestDto) {
        if (departmentRequestDto.getDepartmentName() == null || departmentRequestDto.getDepartmentName().isEmpty() || departmentRequestDto.getDepartmentName().length() == 0) {
            return MessageConfig.DEPARTMENT_NAME_MISSING;
        }
        if (departmentRequestDto.getIsActive() == null) {
            return MessageConfig.DEPARTMENT_STATUS_NOT_PROPER;
        }
        if (departmentRequestDto.getDepartmentId() == null) {
            return MessageConfig.ID_IS_MISSING;
        }
        return null;
    }
}
