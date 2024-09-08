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
@RequestMapping("/desig")
public class DesignationController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DesignationService desigService;

    @Autowired
    private DepartmentService departmentService;

    //LIST-get ,BYID-get  save-post ,delete-get ,updated-put

    @GetMapping("/desigList")
    public ResponseEntity<Object> GetTheListofDesignation(){
        return HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY , HttpStatus.OK ,desigService.getthelistofDesignation());
    }
    @GetMapping("/Designationbyid")
    public ResponseEntity<Object> GetTheDesignationByid(@RequestParam("id") String id){
        Optional<DesignationDetails> desigoptional =  desigService.getthedesignationdetailsbyid(Integer.parseInt(id));
        if(desigoptional.isPresent()){
            return  HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY,HttpStatus.OK,desigoptional.get());
        }
        return  HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND,HttpStatus.NOT_FOUND,null);
    }
    @GetMapping("/deleteDesignation")
    public ResponseEntity<Object> DeletetheDesignationByid(@RequestParam("id") String id){
        String response =  desigService.Deletethedesignationbyid(Integer.parseInt(id));
        return  HandleRequest.createResponse(response,HttpStatus.OK,null);
    }
    @PostMapping("/saveDesig")
    public ResponseEntity<Object>  SaveTheDesignationDetials(@RequestParam("pojo") String DesignationObj) throws JsonProcessingException {
        DesignationRequestDto dto =  objectMapper.readValue(DesignationObj,DesignationRequestDto.class);

        Optional<DepartmentDetails> deptdetails =  departmentService.getthedepartmentbyid(Integer.parseInt(dto.getDeptID()));
        if(deptdetails.isEmpty()){
            return  HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND,HttpStatus.OK ,null);
        }
        DesignationDetails designationDetails =  new DesignationDetails();
        designationDetails.setDesignationname(dto.getDesignationName());
        designationDetails.set_active_status(dto.getIs_Active_status());
        designationDetails.setCreatedon(LocalDateTime.now());
        designationDetails.setCreatedby(1);
        designationDetails.setDepartmentDetails(deptdetails.get());

        return HandleRequest.createResponse(MessageConfig.DESIGNATION_ADDED_SUCCESSFULLY,HttpStatus.CREATED,desigService.savethedesignation(designationDetails));
    }
    @PutMapping("/updateDesignation")
    public ResponseEntity<Object> UpdateTheDesignationDetails(@RequestParam("id") String id,@RequestParam("pojo") String desingation) throws JsonProcessingException {
        DesignationRequestDto designationRequestDto  =  objectMapper.readValue(desingation ,DesignationRequestDto.class);
        Optional<DesignationDetails> desigOptional =  desigService.getthedesignationdetailsbyid(Integer.parseInt(id));
        Optional<DepartmentDetails>   departOptional =  departmentService.getthedepartmentbyid(Integer.parseInt(designationRequestDto.getDeptID()));
        if(departOptional.isEmpty()){
            return  HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND,HttpStatus.OK,null);
        }
        if(desigOptional.isPresent())
        {
            DesignationDetails designationDetails = desigOptional.get();
            designationDetails.setDepartmentDetails(departOptional.get());
            designationDetails.setDesignationname(designationRequestDto.getDesignationName());
            designationDetails.setUpdatedby(1);
            designationDetails.setUpdatedon(LocalDateTime.now());
            designationDetails.set_active_status(designationRequestDto.getIs_Active_status());
            return HandleRequest.createResponse(MessageConfig.DESIGNATION_UPDATED_SUCCESSFULLY,HttpStatus.OK,desigService.savethedesignation(designationDetails));
        }
        return HandleRequest.createResponse(MessageConfig.OPERATION_FAIL ,HttpStatus.OK,null);
    }
}
