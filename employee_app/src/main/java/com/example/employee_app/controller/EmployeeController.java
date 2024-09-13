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
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EmployeeService empService;
    @Autowired
    private DesignationService desigService;
    @Autowired
    private DepartmentService deptService;
    @Autowired
    private EmailSchedularBirthDay emailSchedularBirthDay;
    @Autowired
    private EmailSchedular emailSchedular;

    @GetMapping("/ListOfEmp")
    public ResponseEntity<Object> GetTheListOfEmployees() {
        return HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY, HttpStatus.OK, empService.getTheListOfTheEmployees());
    }

    @GetMapping("/getEmpById")
    public ResponseEntity<Object> GetTheEmployeeByID(@RequestParam("id") String id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        Optional<EmployeeDetails> empOptional = empService.GetTheEmployeeDetailsById(Long.parseLong(id));
        if (empOptional.isPresent()) {
            return HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY, HttpStatus.OK, empOptional.get());
        }
        return HandleRequest.createResponse(MessageConfig.EMPLOYEE_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
    }

    @DeleteMapping("/DeleteEmp")
    public ResponseEntity<Object> DeleteTHeEmployeeById(@RequestParam("id") String id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        ResponseEntity<String> response = empService.deleteTheEmployeeById(Long.parseLong(id));
        return HandleRequest.createResponse(response.getBody(), response.getStatusCode(), null);
    }

    @PostMapping("/SaveTheEmp")
    public ResponseEntity<Object> SaveTheEmployee(@RequestParam("employeedata") String empdetails, @RequestParam("image") MultipartFile imagefile) throws JsonProcessingException, IOException {
        EmployeeRequestDto empdto = objectMapper.readValue(empdetails, EmployeeRequestDto.class);
        String validataioncheck = CheckForTheValidations(empdto);
        if (validataioncheck == null) {
            Optional<DesignationDetails> designationOptionl = desigService.getthedesignationdetailsbyid(Long.parseLong(empdto.getDeignationId()));
            if (designationOptionl.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (!designationOptionl.get().getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_STATUS_IS_NOT_ACTIVE, HttpStatus.OK, null);
            }
            Optional<DepartmentDetails> deptOptional = deptService.getthedepartmentbyid(Long.parseLong(empdto.getDepartmentId()));
            if (deptOptional.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (designationOptionl.get().getDepartmentId() != deptOptional.get().getId()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_MISSED_MATCHED_WITH_DESIGNATION_DEPARTMENT_ID, HttpStatus.NOT_FOUND, null);
            }
            if (!deptOptional.get().getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_STATUS_IS_NOT_ACTIVE, HttpStatus.NOT_FOUND, null);
            }
            if (imagefile.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.IMAGE_IS_MISSING, HttpStatus.NOT_FOUND, null);
            }
            if(!imagefile.getContentType().equals("image/jpeg") || imagefile.getContentType().equals("image/png")){
                return  HandleRequest.createResponse(MessageConfig.IMAGE_DATA_TYPE_NOT_PROPER,HttpStatus.NOT_ACCEPTABLE,null);
            }
            ResponseEntity<Object> response = empService.AddTheEmployeeDetails(empdto, imagefile);
            return HandleRequest.createResponse(MessageConfig.EMPLOYEE_ADDED_SUCCESSFULLY, response.getStatusCode(), response.getBody());
        }
        return HandleRequest.createResponse(MessageConfig.OPERATION_FAIL, HttpStatus.NOT_ACCEPTABLE, validataioncheck);
    }

    @PutMapping("/UpdateEmp")
    public ResponseEntity<Object> UpdateTheEmployee(@RequestParam("employeedata") String empdetails, @RequestParam("image") MultipartFile imagefile) throws JsonProcessingException, IOException {

        EmployeeRequestDto empdto = objectMapper.readValue(empdetails, EmployeeRequestDto.class);
        String validationCheck = CheckValidationForUpdateDto(empdto);
        if (validationCheck != null) {
            return HandleRequest.createResponse(validationCheck, HttpStatus.NOT_FOUND, null);
        }

        Optional<EmployeeDetails> empOptional = empService.GetTheEmployeeDetailsById(Long.parseLong(empdto.getEmployeeId()));
        if (empOptional.isPresent()) {
            EmployeeDetails emp = empOptional.get();
            if (!emp.getEmail().equals(empdto.getEmail())) {
                if (empService.FindTheEmployeeByEmail(empdto.getEmail()).isPresent()) {
                    return HandleRequest.createResponse(MessageConfig.DUPLICATE_EMAIL_IS_PRESENT, HttpStatus.NOT_ACCEPTABLE, null);
                }
            }
            Optional<DesignationDetails> designationOptionl = desigService.getthedesignationdetailsbyid(Long.parseLong(empdto.getDeignationId()));
            if (designationOptionl.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (!designationOptionl.get().getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_STATUS_IS_NOT_ACTIVE, HttpStatus.OK, null);
            }

            Optional<DepartmentDetails> departmentOptional = deptService.getthedepartmentbyid(Long.parseLong(empdto.getDepartmentId()));
            if (departmentOptional.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (!departmentOptional.get().getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_STATUS_IS_NOT_ACTIVE, HttpStatus.NOT_FOUND, null);
            }
            if (imagefile.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.IMAGE_IS_MISSING, HttpStatus.NOT_FOUND, null);
            }
            if(imagefile.getContentType().equals("image/jpeg") || imagefile.getContentType().equals("image/png")){
                return  HandleRequest.createResponse(MessageConfig.IMAGE_DATA_TYPE_NOT_PROPER,HttpStatus.NOT_ACCEPTABLE,null);
            }
            if (designationOptionl.get().getDepartmentId() != departmentOptional.get().getId()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_MISSED_MATCHED_WITH_DESIGNATION_DEPARTMENT_ID, HttpStatus.NOT_FOUND, null);
            }
            ResponseEntity<Object> response = empService.UpdateTheEmployee(empdto, imagefile);
            return HandleRequest.createResponse(MessageConfig.EMPLOYEE_UPDATED_SUCCESSFULLY, HttpStatus.CREATED, response.getBody());
        }
        return HandleRequest.createResponse(MessageConfig.OPERATION_FAIL, HttpStatus.NOT_ACCEPTABLE, null);
    }

    @GetMapping("/email")
    public String SendCustomDateEmail(@RequestParam("month") String month, @RequestParam("day") String day) throws MessagingException {
        emailSchedular.SendTheGreetingOfBirthday_direct(Integer.parseInt(month), Integer.parseInt(day));
        return "done ";
    }

    @GetMapping("/empStatusChange")
    public ResponseEntity<Object> SetTheEmployeeStatusToTrue(@RequestParam("id") String id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        Optional<EmployeeDetails> employeeDetailsOptional = empService.GetTheEmployeeDetailsById(Long.parseLong(id));
        if (employeeDetailsOptional.isPresent()) {
            ResponseEntity<Object> response = empService.ChangeEmployeeStatusToTrue(Long.parseLong(id));
            return HandleRequest.createResponse(MessageConfig.EMPLOYEE_STATUS_CHANGE_TO_ACTIVE, response.getStatusCode(), response.getBody());
        }
        return HandleRequest.createResponse(MessageConfig.OPERATION_FAIL, HttpStatus.NOT_FOUND, MessageConfig.EMPLOYEE_NOT_FOUND);
    }

    ;


    public String CheckForTheValidations(EmployeeRequestDto employeeRequestDto) {

        if (employeeRequestDto.getDeignationId() == null || employeeRequestDto.getDeignationId().isEmpty()) {
            return MessageConfig.DESIGNATION_IS_MISSING;
        }
        if (employeeRequestDto.getDepartmentId() == null || employeeRequestDto.getDepartmentId().isEmpty()) {
            return MessageConfig.DEPARTMENT_IS_MISSING;
        }
        if (employeeRequestDto.getEmployeeName() == null || employeeRequestDto.getEmployeeName().isEmpty()) {
            return MessageConfig.EMP_NAME_MISSING;
        }

        Optional<EmployeeDetails> gettheemaildetails = empService.FindTheEmployeeByEmail(employeeRequestDto.getEmail());
        if (gettheemaildetails.isPresent()) {
            return MessageConfig.DUPLICATE_EMAIL_IS_PRESENT;
        }
        if (empService.CheckIfEmployeeOfSameNamePresentORNot(employeeRequestDto.getEmployeeName())) {
            return MessageConfig.DUPLICATE_EMPLOYEE_NAME;
        }
        return null;
    }

    public Boolean CheckIFIdIsPresentOrNot(String id) {
        if (id == null || id.isEmpty()) {
            return true;
        }
        return false;
    }

    public String CheckValidationForUpdateDto(EmployeeRequestDto employeeRequestDto) {
        if (employeeRequestDto.getDeignationId() == null || employeeRequestDto.getDeignationId().isEmpty()) {
            return MessageConfig.DESIGNATION_IS_MISSING;
        }
        if (employeeRequestDto.getDepartmentId() == null || employeeRequestDto.getDepartmentId().isEmpty()) {
            return MessageConfig.DEPARTMENT_IS_MISSING;
        }
        if (employeeRequestDto.getEmployeeName() == null || employeeRequestDto.getEmployeeName().isEmpty()) {
            return MessageConfig.EMP_NAME_MISSING;
        }
        if (CheckIFIdIsPresentOrNot(employeeRequestDto.getEmployeeId())) {
            return MessageConfig.ID_IS_MISSING;
        }
        return null;
    }
}


