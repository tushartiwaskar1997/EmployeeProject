package com.example.employee_app.controller;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.DesignationRequestDto;
import com.example.employee_app.dto.HandleRequest;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.service.DepartmentService;
import com.example.employee_app.service.DesignationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/designations")
public class DesignationController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DesignationService desigService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/GetDesignationList")
    public ResponseEntity<Object> GetTheListOfDesignation() {
        return HandleRequest.createResponse(
                MessageConfig.OPERATION_DONE_SUCCESSFULLY,
                HttpStatus.OK
                , desigService.getthelistofDesignation());
    }

    @GetMapping("/DesignationById")
    public ResponseEntity<Object> GetTheDesignationById(@RequestParam("id") String id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        Optional<DesignationDetails> desigoptional = desigService.getthedesignationdetailsbyid(Long.parseLong(id));
        if (desigoptional.isPresent()) {
            return HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY, HttpStatus.OK, desigoptional.get());
        }
        return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
    }

    @DeleteMapping("/deleteDesignation")
    public ResponseEntity<Object> DeleteTheDesignationById(@RequestParam("id") String id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        Optional<DesignationDetails> designationDetailsOptional = desigService.getthedesignationdetailsbyid(Long.parseLong(id));
        if (!designationDetailsOptional.get().getIsActive()) {
            return HandleRequest.createResponse(MessageConfig.DESIGNATION_ALREADY_DELETED, HttpStatus.NOT_ACCEPTABLE, null);
        }
        String response = desigService.Deletethedesignationbyid(Long.parseLong(id));
        return HandleRequest.createResponse(response, HttpStatus.OK, null);
    }

    @PostMapping("/saveDesignation")
    public ResponseEntity<Object> SaveTheDesignationDetails(@RequestParam("designationdata") String designation) throws JsonProcessingException {
        DesignationRequestDto DesignationDto = objectMapper.readValue(designation, DesignationRequestDto.class);
        String checkforValidaion = CheckValidations(DesignationDto);
        if (checkforValidaion == null) {
            Optional<DepartmentDetails> deptdetails = departmentService.getthedepartmentbyid(Long.parseLong(DesignationDto.getDepartmentId()));
            if (deptdetails.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            ResponseEntity<Object> response = desigService.AddTheDesignation(DesignationDto);
            return HandleRequest.createResponse(MessageConfig.DESIGNATION_ADDED_SUCCESSFULLY, HttpStatus.OK, response.getBody());
        }
        return HandleRequest.createResponse(MessageConfig.OPERATION_FAIL, HttpStatus.NOT_ACCEPTABLE, checkforValidaion);
    }

    @PutMapping("/updateDesignation")
    public ResponseEntity<Object> UpdateTheDesignationDetails(@RequestParam("designationdata") String desingationdto) throws JsonProcessingException {

        DesignationRequestDto designationRequestDto = objectMapper.readValue(desingationdto, DesignationRequestDto.class);
        String validationResponse = CheckValidationForTheUpdate(designationRequestDto);
        if (validationResponse != null) {
            return HandleRequest.createResponse(validationResponse, HttpStatus.NOT_FOUND, null);
        }
        Optional<DesignationDetails> desigOptional = desigService.getthedesignationdetailsbyid(Long.parseLong(designationRequestDto.getDesignationId()));
        Optional<DepartmentDetails> departOptional = departmentService.getthedepartmentbyid(Long.parseLong(designationRequestDto.getDepartmentId()));
        if (departOptional.isEmpty()) {
            return HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }
        if (desigOptional.isPresent()) {
            if (!desigOptional.get().getDesignationName().equals(designationRequestDto.getDesignationName())) {
                if (desigService.CheckIfDuplicateDesignationIsAvaliableForthatDepartment(Long.parseLong(designationRequestDto.getDepartmentId()), designationRequestDto.getDesignationName())) {
                    return HandleRequest.createResponse(MessageConfig.DESIGNATION_NAME_DUPLICATE, HttpStatus.OK, null);
                }
            }
            if (!designationRequestDto.getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_STATUS_CANNOT_SET_inACTIVE, HttpStatus.NOT_ACCEPTABLE, null);
            }
            ResponseEntity<Object> response = desigService.UpdateTheDesignation(designationRequestDto);
            return HandleRequest.createResponse(MessageConfig.DESIGNATION_UPDATED_SUCCESSFULLY, HttpStatus.OK, response.getBody());
        }
        return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
    }

    public String CheckValidations(DesignationRequestDto designationdto) {

        if (designationdto.getDepartmentId() == null || designationdto.getDepartmentId().isEmpty()) {
            return MessageConfig.DEPARTMENT_NOT_FOUND;
        }
        if (designationdto.getDesignationName() == null || designationdto.getDesignationName().isEmpty()) {
            return MessageConfig.DESIGNATION_NAME_MISSING;
        }

        if (desigService.CheckIfDuplicateDesignationIsAvaliableForthatDepartment(Long.parseLong(designationdto.getDepartmentId()), designationdto.getDesignationName())) {
            return MessageConfig.DESIGNATION_NAME_DUPLICATE;
        }
        return null;
    }

    public Boolean CheckIFIdIsPresentOrNot(String id) {
        if (id == null || id.isEmpty()) {
            return true;
        }
        return false;
    }

    public String CheckValidationForTheUpdate(DesignationRequestDto designationRequestDto) {
        if (designationRequestDto.getDesignationId() == null || designationRequestDto.getDesignationId().isEmpty()) {
            return MessageConfig.DESIGNATION_IS_MISSING;
        }
        if (designationRequestDto.getDesignationName() == null || designationRequestDto.getDesignationName().isEmpty()) {
            return MessageConfig.DESIGNATION_NAME_MISSING;
        }
        if (designationRequestDto.getDepartmentId() == null || designationRequestDto.getDepartmentId().isEmpty()) {
            return MessageConfig.DEPARTMENT_IS_MISSING;
        }
        if (designationRequestDto.getIsActive() == null) {
            return MessageConfig.STATUS_IS_MISSING;
        }
        if (CheckIFIdIsPresentOrNot(designationRequestDto.getDesignationId())) {
            return MessageConfig.ID_IS_MISSING;
        }
        return null;
    }
}
