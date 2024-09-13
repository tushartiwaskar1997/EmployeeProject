package com.example.employee_app.controller;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.DepartmentRequestDto;
import com.example.employee_app.dto.HandleRequest;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.service.DepartmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.type.CharacterNCharType;
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
    public ResponseEntity<Object> GetTheListofDepartments() {
        return HandleRequest.createResponse(
                MessageConfig.OPERATION_DONE_SUCCESSFULLY,
                HttpStatus.OK,
                departmentService.getthedepartmentlist());
    }

    @GetMapping("/getDeptById")
    public ResponseEntity<Object> GetTheDeptDetailsById(@RequestParam("id") String id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        Optional<DepartmentDetails> deptoptional = departmentService.getthedepartmentbyid(Long.parseLong(id));
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

    @DeleteMapping("/deleteDept")
    public ResponseEntity<Object> DeleteDeptByID(@RequestParam("id") String id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        String response = departmentService.deletethedepartmentbyid(Long.parseLong(id));
        return HandleRequest.createResponse(response, HttpStatus.OK, null);
    }

    @PostMapping("/saveDept")
    public ResponseEntity<Object> SaveTheDeptDetails(@RequestParam("departmentdata") String Deptdetails) throws JsonProcessingException {
        DepartmentRequestDto deptdto = objectMapper.readValue(Deptdetails, DepartmentRequestDto.class);
        String checkvalidation = CheckValidation(deptdto);
        if (checkvalidation == null) {
            ResponseEntity<Object> response = departmentService.AddTheDepartment(deptdto);
            return HandleRequest.createResponse(MessageConfig.DEPARTMENT_ADDED_SUCCESSFULLY, response.getStatusCode(), response.getBody());
        }
        return HandleRequest.createResponse(MessageConfig.OPERATION_FAIL, HttpStatus.NOT_ACCEPTABLE, checkvalidation);
    }

    @PutMapping("/updateDept")
    public ResponseEntity<Object> UpdateTheDepartmentDetails(@RequestParam("departmentdata") String departmentDetails) throws JsonProcessingException {

        DepartmentRequestDto departmentDtoObj = objectMapper.readValue(departmentDetails, DepartmentRequestDto.class);
        String validationCheck = CheckValidationsAtUpdate(departmentDtoObj);
        if (validationCheck != null) {
            return HandleRequest.createResponse(validationCheck, HttpStatus.NOT_ACCEPTABLE, null);
        }

        Optional<DepartmentDetails> deptoptional = departmentService.getthedepartmentbyid(Long.parseLong(departmentDtoObj.getDepartmentId()));
        if (deptoptional.isPresent()) {

            if (!departmentDtoObj.getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_STATUS_CANNOT_SET_inACTIVE, HttpStatus.NOT_ACCEPTABLE, null);
            }

            ResponseEntity<Object> response = departmentService.UpdateTheDepartment(departmentDtoObj);
            if (response.getStatusCode() != HttpStatus.OK) {
                return HandleRequest.createResponse(response.getBody().toString(), response.getStatusCode(), null);
            } else {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_UPDATED_SUCCESSFULLY, response.getStatusCode(), response.getBody());
            }
        }
        return HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
    }

    public String CheckValidation(DepartmentRequestDto deptdto) {
        if (deptdto.getDepartmentName() == null || deptdto.getDepartmentName().isEmpty() || deptdto.getDepartmentName().length() == 0) {
            return MessageConfig.DEPARTMENT_NAME_MISSING;
        }

        Optional<DepartmentDetails> ChecKIfDepartmentNameISPresentOrNot = departmentService.chekifDepartmentispresent(deptdto.getDepartmentName());
        if (ChecKIfDepartmentNameISPresentOrNot.isPresent()) {
            return MessageConfig.DEPARTMENT_ALREADY_EXIST;
        }
        return null;
    }

    public Boolean CheckIFIdIsPresentOrNot(String id) {
        if (id == null || id.isEmpty()) {
            return true;
        }
        return false;
    }

    public String CheckValidationsAtUpdate(DepartmentRequestDto departmentRequestDto) {
        if (departmentRequestDto.getDepartmentName() == null || departmentRequestDto.getDepartmentName().isEmpty() || departmentRequestDto.getDepartmentName().length() == 0) {
            return MessageConfig.DEPARTMENT_NAME_MISSING;
        }
        if (departmentRequestDto.getIsActive() == null) {
            return MessageConfig.DEPARTMENT_STATUS_NOT_PROPER;
        }
        if (CheckIFIdIsPresentOrNot(departmentRequestDto.getDepartmentId())) {
            return MessageConfig.ID_IS_MISSING;
        }
        return null;
    }
}
