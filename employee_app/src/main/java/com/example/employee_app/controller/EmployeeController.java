package com.example.employee_app.controller;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.EmployeeRequestDto;
import com.example.employee_app.dto.ResponseHandler;
import com.example.employee_app.email.EmailSchedularBirthDay;
import com.example.employee_app.email.EmployeeRelatedEmail;
import com.example.employee_app.email.EmployeeDeleteEmail;
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


import java.util.Map;
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
    @Autowired
    private EmployeeRelatedEmail employeeAddedEmail;
    @Autowired
    private EmployeeDeleteEmail employeeDeleteEmail;

    @GetMapping("/ListOfEmp")
    public ResponseEntity<Object> GetTheListOfEmployees() {
        return ResponseHandler.createResponse(MessageConfig.SUCCESS_OPERATION, HttpStatus.OK, empService.getTheListOfTheEmployees());
    }

    @GetMapping("/getEmpById/{EmpId}")
    public ResponseEntity<Object> GetTheEmployeeByID(@PathVariable("EmpId") Long id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return ResponseHandler.createResponse(MessageConfig.ERROR_ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        Optional<EmployeeDetails> empOptional = empService.GetTheEmployeeDetailsById(id);
        if (empOptional.isPresent()) {
            return ResponseHandler.createResponse(MessageConfig.SUCCESS_OPERATION, HttpStatus.OK, empOptional.get());
        }
        return ResponseHandler.createResponse(MessageConfig.ERROR_EMPLOYEE_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
    }

    @DeleteMapping("/DeleteEmp/{EmpID}")
    public ResponseEntity<Object> DeleteTHeEmployeeById(@PathVariable("EmpID") Long id) throws MessagingException {

        Optional<EmployeeDetails> Employeeoptional = empService.GetTheEmployeeDetailsById(id);
        if (Employeeoptional.isPresent()) {
            EmployeeDetails Employee = Employeeoptional.get();
            if (!Employee.getIsActive()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_EMPLOYEE_ALREADY_DELETED, HttpStatus.OK, null);
            }
            return ResponseHandler.createResponse(empService.deleteTheEmployeeById(id), HttpStatus.ACCEPTED, null);
        }
        return new ResponseEntity<>(MessageConfig.ERROR_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/SaveTheEmp")
    public ResponseEntity<Object> SaveTheEmployee(@RequestParam("employeeData") String empdetails, @RequestParam("image") MultipartFile imagefile) throws JsonProcessingException, IOException, MessagingException {

        EmployeeRequestDto empdto = objectMapper.readValue(empdetails, EmployeeRequestDto.class);
        String validataioncheck = CheckForTheValidations(empdto);
        if (validataioncheck == null) {
            Optional<EmployeeDetails> gettheemaildetails = empService.FindTheEmployeeByEmail(empdto.getEmail());
            if (gettheemaildetails.isPresent()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DUPLICATE_EMAIL_IS_PRESENT, HttpStatus.NOT_FOUND, null);
            }
            if (empService.CheckIfEmployeeOfSameNamePresentORNot(empdto.getEmployeeName())) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DUPLICATE_EMPLOYEE_NAME, HttpStatus.NOT_FOUND, null);
            }
            Optional<DesignationDetails> designationOptionl = desigService.GetTheDesignationDetailsById((empdto.getDesignationId()));
            if (designationOptionl.isEmpty()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (!designationOptionl.get().getIsActive()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DESIGNATION_STATUS_IS_NOT_ACTIVE, HttpStatus.OK, null);
            }
            Optional<DepartmentDetails> deptOptional = deptService.GetTheDepartmentById((empdto.getDepartmentId()));
            if (deptOptional.isEmpty()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (!deptOptional.get().getIsActive()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DEPARTMENT_STATUS_IS_NOT_ACTIVE, HttpStatus.NOT_FOUND, null);
            }
            if (imagefile.isEmpty()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_IMAGE_IS_MISSING, HttpStatus.NOT_FOUND, null);
            }
            if (!(imagefile.getContentType().equals("image/jpeg") || imagefile.getContentType().equals("image/png"))) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_IMAGE_DATA_TYPE_NOT_PROPER, HttpStatus.NOT_ACCEPTABLE, null);
            }
            ResponseEntity<Object> response = empService.AddTheEmployeeDetails(empdto, imagefile);

            return ResponseHandler.createResponse(MessageConfig.SUCCESS_EMPLOYEE_ADDED, response.getStatusCode(), response.getBody());
        }
        return ResponseHandler.createResponse(validataioncheck, HttpStatus.NOT_ACCEPTABLE, null);
    }

    @PutMapping("/UpdateEmp")
    public ResponseEntity<Object> UpdateTheEmployee(@RequestParam("employeeData") String empdetails, @RequestParam("image") MultipartFile imagefile) throws JsonProcessingException, IOException {
        EmployeeRequestDto empdto = objectMapper.readValue(empdetails, EmployeeRequestDto.class);
        String validationCheck = CheckValidationForUpdateDto(empdto);
        if (validationCheck != null) {
            return ResponseHandler.createResponse(validationCheck, HttpStatus.NOT_FOUND, null);
        }

        Optional<EmployeeDetails> empOptional = empService.GetTheEmployeeDetailsById((empdto.getEmployeeId()));
        if (empOptional.isPresent()) {
            EmployeeDetails emp = empOptional.get();
            if (!emp.getEmpName().equals(empdto.getEmployeeName())) {
                if (empService.CheckIfEmployeeOfSameNamePresentORNot(empdto.getEmployeeName())) {
                    return ResponseHandler.createResponse(MessageConfig.ERROR_DUPLICATE_EMPLOYEE_NAME, HttpStatus.NOT_ACCEPTABLE, null);
                }
            }
            if (!emp.getEmail().equals(empdto.getEmail())) {
                if (empService.FindTheEmployeeByEmail(empdto.getEmail()).isPresent()) {
                    return ResponseHandler.createResponse(MessageConfig.ERROR_DUPLICATE_EMAIL_IS_PRESENT, HttpStatus.NOT_ACCEPTABLE, null);
                }
            }
            Optional<DesignationDetails> designationOptionl = desigService.GetTheDesignationDetailsById((empdto.getDesignationId()));
            if (designationOptionl.isEmpty()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (!designationOptionl.get().getIsActive()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DESIGNATION_STATUS_IS_NOT_ACTIVE, HttpStatus.OK, null);
            }
            Optional<DepartmentDetails> departmentOptional = deptService.GetTheDepartmentById((empdto.getDepartmentId()));
            if (departmentOptional.isEmpty()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (!departmentOptional.get().getIsActive()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DEPARTMENT_STATUS_IS_NOT_ACTIVE, HttpStatus.NOT_FOUND, null);
            }
            if (imagefile.isEmpty()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_IMAGE_IS_MISSING, HttpStatus.NOT_FOUND, null);
            }
            if (!(imagefile.getContentType().equals("image/jpeg") || imagefile.getContentType().equals("image/png"))) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_IMAGE_DATA_TYPE_NOT_PROPER, HttpStatus.NOT_ACCEPTABLE, null);
            }
            ResponseEntity<Object> response = empService.UpdateTheEmployee(empdto, imagefile);
            return ResponseHandler.createResponse(MessageConfig.SUCCESS_EMPLOYEE_UPDATED, HttpStatus.CREATED, response.getBody());
        }
        return ResponseHandler.createResponse(MessageConfig.ERROR_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE, null);
    }

    @GetMapping("/email")
    public ResponseEntity<Object> SendCustomDateEmail(@RequestParam("month") String month, @RequestParam("day") String day) throws MessagingException {
        String response = emailSchedular.SendTheGreetingOfBirthday_direct(Integer.parseInt(month), Integer.parseInt(day));
        return ResponseHandler.createResponse(response, HttpStatus.NOT_ACCEPTABLE, null);
    }

    @PostMapping("/empStatusChangeToActive")
    public ResponseEntity<Object> SetTheEmployeeStatusToTrue(@RequestBody Map<String, Long> details) {

        if (CheckIFIdIsPresentOrNot(details.get("EmployeeId"))) {
            return ResponseHandler.createResponse(MessageConfig.ERROR_ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        Optional<EmployeeDetails> employeeDetailsOptional = empService.GetTheEmployeeDetailsById(details.get("EmployeeId"));
        if (employeeDetailsOptional.isPresent()) {
            if (employeeDetailsOptional.get().getIsActive()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_EMPLOYEE_IS_ALREADY_ACTIVE, HttpStatus.NOT_ACCEPTABLE, null);
            }

            if (!employeeDetailsOptional.get().getDepartmentDetails().getIsActive()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DEPARTMENT_STATUS_IS_NOT_ACTIVE, HttpStatus.NOT_ACCEPTABLE, null);
            }
            if (!employeeDetailsOptional.get().getDesignationDetails().getIsActive()) {
                return ResponseHandler.createResponse(MessageConfig.ERROR_DESIGNATION_STATUS_IS_NOT_ACTIVE, HttpStatus.NOT_ACCEPTABLE, null);
            }

            ResponseEntity<Object> response = empService.ChangeEmployeeStatusToTrue(details.get("id"));
            return ResponseHandler.createResponse(MessageConfig.ERROR_EMPLOYEE_STATUS_CHANGE_TO_ACTIVE, response.getStatusCode(), response.getBody());
        }
        return ResponseHandler.createResponse(MessageConfig.ERROR_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND, null);
    }

    public String CheckForTheValidations(EmployeeRequestDto employeeRequestDto) {

        if (employeeRequestDto.getDesignationId() == null) {
            return MessageConfig.ERROR_DESIGNATION_IS_MISSING;
        }
        if (employeeRequestDto.getDepartmentId() == null) {
            return MessageConfig.ERROR_DEPARTMENT_IS_MISSING;
        }
        if (employeeRequestDto.getEmployeeName() == null || employeeRequestDto.getEmployeeName().isEmpty()) {
            return MessageConfig.ERROR_EMP_NAME_MISSING;
        }
        if (employeeRequestDto.getEmail() == null || employeeRequestDto.getEmail().isEmpty()) {
            return MessageConfig.ERROR_EMAIL_NOT_FOUND;
        }
        return null;
    }

    public Boolean CheckIFIdIsPresentOrNot(Long id) {
        if (id == null) {
            return true;
        }
        return false;
    }

    public String CheckValidationForUpdateDto(EmployeeRequestDto employeeRequestDto) {
        if (employeeRequestDto.getDesignationId() == null) {
            return MessageConfig.ERROR_DESIGNATION_IS_MISSING;
        }
        if (employeeRequestDto.getDepartmentId() == null) {
            return MessageConfig.ERROR_DEPARTMENT_IS_MISSING;
        }
        if (employeeRequestDto.getEmployeeName() == null || employeeRequestDto.getEmployeeName().isEmpty()) {
            return MessageConfig.ERROR_EMP_NAME_MISSING;
        }
        if (employeeRequestDto.getEmail() == null || employeeRequestDto.getEmail().isEmpty()) {
            return MessageConfig.ERROR_EMAIL_NOT_FOUND;
        }
        if (CheckIFIdIsPresentOrNot((employeeRequestDto.getEmployeeId()))) {
            return MessageConfig.ERROR_ID_IS_MISSING;
        }
        return null;
    }
}


