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

    @GetMapping("/DesignationById/{DesignationID}")
    public ResponseEntity<Object> GetTheDesignationById(@PathVariable("DesignationID") Long id) {
//        if (CheckIFIdIsPresentOrNot(id)) {
//            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
//        }
        Optional<DesignationDetails> desigoptional = desigService.getthedesignationdetailsbyid(id);
        if (desigoptional.isPresent()) {
            return HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY, HttpStatus.OK, desigoptional.get());
        }
        return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
    }

    @DeleteMapping("/deleteDesignation/{DesignationID}")
    public ResponseEntity<Object> DeleteTheDesignationById(@PathVariable("DesignationID") Long id) {
//        if (CheckIFIdIsPresentOrNot(id)) {
//            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
//        }
        Optional<DesignationDetails> designationDetailsOptional = desigService.getthedesignationdetailsbyid((id));
        if(designationDetailsOptional.isEmpty()){
            return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!designationDetailsOptional.get().getIsActive()) {
            return HandleRequest.createResponse(MessageConfig.DESIGNATION_ALREADY_DELETED, HttpStatus.NOT_ACCEPTABLE, null);
        }
        String response = desigService.Deletethedesignationbyid((id));
        return HandleRequest.createResponse(response, HttpStatus.OK, null);
    }

    @PostMapping("/saveDesignation")
    public ResponseEntity<Object> SaveTheDesignationDetails(@RequestBody DesignationRequestDto designation) throws JsonProcessingException {

        String checkforValidaion = CheckValidations(designation);
        if (checkforValidaion == null) {

            ResponseEntity<Object> response = desigService.AddTheDesignation(designation);
            return HandleRequest.createResponse(MessageConfig.DESIGNATION_ADDED_SUCCESSFULLY, HttpStatus.OK, response.getBody());
        }
        return HandleRequest.createResponse(checkforValidaion, HttpStatus.NOT_ACCEPTABLE, null);
    }

    @PutMapping("/updateDesignation")
    public ResponseEntity<Object> UpdateTheDesignationDetails(@RequestBody DesignationRequestDto desingationdto) throws JsonProcessingException {

        String validationResponse = CheckValidationForTheUpdate(desingationdto);
        if (validationResponse != null) {
            return HandleRequest.createResponse(validationResponse, HttpStatus.NOT_FOUND, null);
        }

        Optional<DesignationDetails> desigOptional = desigService.getthedesignationdetailsbyid((desingationdto.getDesignationId()));

        if (desigOptional.isPresent()) {
            if (!desigOptional.get().getDesignationName().equals(desingationdto.getDesignationName())) {
                if (desigService.CheckIfDesignatioNmaeExistorNot(desingationdto.getDesignationName()).isPresent()) {
                    return HandleRequest.createResponse(MessageConfig.DESIGNATION_NAME_DUPLICATE, HttpStatus.OK, null);
                }
            }
            if (!desingationdto.getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_STATUS_CANNOT_SET_inACTIVE, HttpStatus.NOT_ACCEPTABLE, null);
            }
            ResponseEntity<Object> response = desigService.UpdateTheDesignation(desingationdto);
            return HandleRequest.createResponse(MessageConfig.DESIGNATION_UPDATED_SUCCESSFULLY, HttpStatus.OK, response.getBody());
        }
        return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
    }

    public String CheckValidations(DesignationRequestDto designationdto) {

        if (designationdto.getDesignationName() == null || designationdto.getDesignationName().isEmpty()) {
            return MessageConfig.DESIGNATION_NAME_MISSING;
        }

        if (desigService.CheckIfDesignatioNmaeExistorNot(designationdto.getDesignationName()).isPresent()) {
            return MessageConfig.DESIGNATION_NAME_DUPLICATE;
        }
        return null;
    }

//    public Boolean CheckIFIdIsPresentOrNot(String id) {
//        if (id == null || id.isEmpty()) {
//            return true;
//        }
//        return false;
//    }

    public String CheckValidationForTheUpdate(DesignationRequestDto designationRequestDto) {
        if (designationRequestDto.getDesignationId() == null ) {
            return MessageConfig.DESIGNATION_IS_MISSING;
        }
        if (designationRequestDto.getDesignationName() == null  || designationRequestDto.getDesignationName().isEmpty()) {
            return MessageConfig.DESIGNATION_NAME_MISSING;
        }
        if (designationRequestDto.getIsActive() == null) {
            return MessageConfig.STATUS_IS_MISSING;
        }
//        if (CheckIFIdIsPresentOrNot(designationRequestDto.getDesignationId())) {
//            return MessageConfig.ID_IS_MISSING;
//        }
        return null;
    }
}
