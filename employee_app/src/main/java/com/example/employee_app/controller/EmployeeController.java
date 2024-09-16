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
import javax.validation.Valid;
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

    @GetMapping("/getEmpById/{EmpId}")
    public ResponseEntity<Object> GetTheEmployeeByID(@PathVariable("EmpId") Long id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        Optional<EmployeeDetails> empOptional = empService.GetTheEmployeeDetailsById(id);
        if (empOptional.isPresent()) {
            return HandleRequest.createResponse(MessageConfig.OPERATION_DONE_SUCCESSFULLY, HttpStatus.OK, empOptional.get());
        }
        return HandleRequest.createResponse(MessageConfig.EMPLOYEE_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
    }

    @DeleteMapping("/DeleteEmp/{EmpID}")
    public ResponseEntity<Object> DeleteTHeEmployeeById(@PathVariable("EmpID") Long id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        ResponseEntity<String> response = empService.deleteTheEmployeeById(id);
        return HandleRequest.createResponse(response.getBody(), response.getStatusCode(), null);
    }

    @PostMapping("/SaveTheEmp")
    public ResponseEntity<Object> SaveTheEmployee(@RequestParam("employeeData") String empdetails, @RequestParam("image") MultipartFile imagefile) throws JsonProcessingException, IOException {


        EmployeeRequestDto empdto = objectMapper.readValue(empdetails, EmployeeRequestDto.class);
        String validataioncheck = CheckForTheValidations(empdto);

        if (validataioncheck == null) {

            Optional<EmployeeDetails> gettheemaildetails = empService.FindTheEmployeeByEmail(empdto.getEmail());
            if (gettheemaildetails.isPresent()) {
                return HandleRequest.createResponse(MessageConfig.DUPLICATE_EMAIL_IS_PRESENT, HttpStatus.NOT_FOUND, null);
            }
            if (empService.CheckIfEmployeeOfSameNamePresentORNot(empdto.getEmployeeName())) {
                return HandleRequest.createResponse(MessageConfig.DUPLICATE_EMPLOYEE_NAME, HttpStatus.NOT_FOUND, null);
            }

            Optional<DesignationDetails> designationOptionl = desigService.getthedesignationdetailsbyid((empdto.getDesignationId()));
            if (designationOptionl.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (!designationOptionl.get().getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_STATUS_IS_NOT_ACTIVE, HttpStatus.OK, null);
            }
            Optional<DepartmentDetails> deptOptional = deptService.getthedepartmentbyid((empdto.getDepartmentId()));
            if (deptOptional.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }

            if (!deptOptional.get().getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_STATUS_IS_NOT_ACTIVE, HttpStatus.NOT_FOUND, null);
            }
            if (imagefile.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.IMAGE_IS_MISSING, HttpStatus.NOT_FOUND, null);
            }
            if (!(imagefile.getContentType().equals("image/jpeg") || imagefile.getContentType().equals("image/png"))) {
                return HandleRequest.createResponse(MessageConfig.IMAGE_DATA_TYPE_NOT_PROPER, HttpStatus.NOT_ACCEPTABLE, null);
            }
            ResponseEntity<Object> response = empService.AddTheEmployeeDetails(empdto, imagefile);
            return HandleRequest.createResponse(MessageConfig.EMPLOYEE_ADDED_SUCCESSFULLY, response.getStatusCode(), response.getBody());
        }
        return HandleRequest.createResponse(validataioncheck, HttpStatus.NOT_ACCEPTABLE, null);
    }

    @PutMapping("/UpdateEmp")
    public ResponseEntity<Object> UpdateTheEmployee( @RequestParam("employeeData") String empdetails, @RequestParam("image") MultipartFile imagefile) throws JsonProcessingException, IOException {
        EmployeeRequestDto empdto = objectMapper.readValue(empdetails, EmployeeRequestDto.class);
        String validationCheck = CheckValidationForUpdateDto(empdto);
        if (validationCheck != null) {
            return HandleRequest.createResponse(validationCheck, HttpStatus.NOT_FOUND, null);
        }

        Optional<EmployeeDetails> empOptional = empService.GetTheEmployeeDetailsById((empdto.getEmployeeId()));
        if (empOptional.isPresent()) {
            EmployeeDetails emp = empOptional.get();

            if (!emp.getEmpName().equals(empdto.getEmployeeName())) {
                if (empService.CheckIfEmployeeOfSameNamePresentORNot(empdto.getEmployeeName())) {
                    return HandleRequest.createResponse(MessageConfig.DUPLICATE_EMPLOYEE_NAME, HttpStatus.NOT_ACCEPTABLE, null);
                }
            }
            if (!emp.getEmail().equals(empdto.getEmail())) {
                if (empService.FindTheEmployeeByEmail(empdto.getEmail()).isPresent()) {
                    return HandleRequest.createResponse(MessageConfig.DUPLICATE_EMAIL_IS_PRESENT, HttpStatus.NOT_ACCEPTABLE, null);
                }
            }
            Optional<DesignationDetails> designationOptionl = desigService.getthedesignationdetailsbyid((empdto.getDesignationId()));
            if (designationOptionl.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (!designationOptionl.get().getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DESIGNATION_STATUS_IS_NOT_ACTIVE, HttpStatus.OK, null);
            }

            Optional<DepartmentDetails> departmentOptional = deptService.getthedepartmentbyid((empdto.getDepartmentId()));
            if (departmentOptional.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            if (!departmentOptional.get().getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.DEPARTMENT_STATUS_IS_NOT_ACTIVE, HttpStatus.NOT_FOUND, null);
            }
            if (imagefile.isEmpty()) {
                return HandleRequest.createResponse(MessageConfig.IMAGE_IS_MISSING, HttpStatus.NOT_FOUND, null);
            }
            if (!(imagefile.getContentType().equals("image/jpeg") || imagefile.getContentType().equals("image/png"))) {
                return HandleRequest.createResponse(MessageConfig.IMAGE_DATA_TYPE_NOT_PROPER, HttpStatus.NOT_ACCEPTABLE, null);
            }
            if(!emp.getIsActive())
            {
                if(emp.getDesignationDetails().getDesignationId()!= empdto.getDesignationId())
                {
                    return HandleRequest.createResponse(MessageConfig.EMPLOYEE_STATUS_IS_NOT_ACTIVE_DESIGNATION_NOT_CHANGE ,HttpStatus.NOT_ACCEPTABLE,null);
                }

                if(emp.getDepartmentDetails().getId() != empdto.getDepartmentId()){
                    return HandleRequest.createResponse(MessageConfig.EMPLOYEE_STATUS_IS_NOT_ACTIVE_DEPARTMENT_NOT_CHANGE ,HttpStatus.NOT_ACCEPTABLE,null);
                }
            }
            ResponseEntity<Object> response = empService.UpdateTheEmployee(empdto, imagefile);
            return HandleRequest.createResponse(MessageConfig.EMPLOYEE_UPDATED_SUCCESSFULLY, HttpStatus.CREATED, response.getBody());
        }
        return HandleRequest.createResponse(MessageConfig.EMPLOYEE_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE, null);
    }

    @GetMapping("/email")
    public String SendCustomDateEmail(@RequestParam("month") String month, @RequestParam("day") String day) throws MessagingException {
        emailSchedular.SendTheGreetingOfBirthday_direct(Integer.parseInt(month), Integer.parseInt(day));
        return "done ";
    }

    @GetMapping("/empStatusChange/{EmpID}")
    public ResponseEntity<Object> SetTheEmployeeStatusToTrue(@PathVariable("EmpID") Long id) {
        if (CheckIFIdIsPresentOrNot(id)) {
            return HandleRequest.createResponse(MessageConfig.ID_IS_MISSING, HttpStatus.NOT_FOUND, null);
        }
        Optional<EmployeeDetails> employeeDetailsOptional = empService.GetTheEmployeeDetailsById(id);
        if (employeeDetailsOptional.isPresent()) {
            if (employeeDetailsOptional.get().getIsActive()) {
                return HandleRequest.createResponse(MessageConfig.EMPLOYEE_IS_ALREADY_ACTIVE, HttpStatus.NOT_ACCEPTABLE, null);
            }
            ResponseEntity<Object> response = empService.ChangeEmployeeStatusToTrue(id);
            return HandleRequest.createResponse(MessageConfig.EMPLOYEE_STATUS_CHANGE_TO_ACTIVE, response.getStatusCode(), response.getBody());
        }
        return HandleRequest.createResponse(MessageConfig.EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND, null);
    }

    ;

    public String CheckForTheValidations(EmployeeRequestDto employeeRequestDto) {

        if (employeeRequestDto.getDesignationId() == null ) {
            return MessageConfig.DESIGNATION_IS_MISSING;
        }
        if (employeeRequestDto.getDepartmentId() == null ) {
            return MessageConfig.DEPARTMENT_IS_MISSING;
        }
        if (employeeRequestDto.getEmployeeName() == null || employeeRequestDto.getEmployeeName().isEmpty()) {
            return MessageConfig.EMP_NAME_MISSING;
        }
        if (employeeRequestDto.getEmail() == null || employeeRequestDto.getEmail().isEmpty()) {
            return MessageConfig.EMAIL_NOT_FOUND;
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
            return MessageConfig.DESIGNATION_IS_MISSING;
        }
        if (employeeRequestDto.getDepartmentId() == null) {
            return MessageConfig.DEPARTMENT_IS_MISSING;
        }
        if (employeeRequestDto.getEmployeeName() == null || employeeRequestDto.getEmployeeName().isEmpty()) {
            return MessageConfig.EMP_NAME_MISSING;
        }
        if (employeeRequestDto.getEmail() == null || employeeRequestDto.getEmail().isEmpty()) {
            return MessageConfig.EMAIL_NOT_FOUND;
        }
        if (CheckIFIdIsPresentOrNot((employeeRequestDto.getEmployeeId()))) {
            return MessageConfig.ID_IS_MISSING;
        }
        return null;
    }
}


