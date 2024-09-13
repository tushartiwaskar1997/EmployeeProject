package com.example.employee_app.service;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.DesignationRequestDto;
import com.example.employee_app.dto.HandleRequest;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.repository.DesignationRepository;
import com.example.employee_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DesignationService {

    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentService departmentService;

    public List<DesignationDetails> getthelistofDesignation() {
        return designationRepository.findAll();
    }

    public Optional<DesignationDetails> getthedesignationdetailsbyid(Long id) {
        return designationRepository.findById(id);
    }

    public String Deletethedesignationbyid(Long id) {
        Optional<DesignationDetails> designationOptional = getthedesignationdetailsbyid(id);
        if (designationOptional.isPresent()) {
            DesignationDetails designationDetails = designationOptional.get();
            if (designationDetails.getTotalEmployee() == 0) {
                designationDetails.setIsActive(false);
                designationDetails.setUpdatedBy("Admin");
                designationDetails.setUpdatedDate(LocalDateTime.now());
                designationRepository.save(designationDetails);
                return MessageConfig.DESIGNATION_DELETED_SUCCESSFULLY;
            } else {
                return MessageConfig.DESIGNATION_CANNOT_BE_DELETED; //+"total Employee Count is "+designationOptional.get().getTotalEmployee;
            }
        }
        return MessageConfig.DESIGNATION_NOT_FOUND;
    }

    public ResponseEntity<Object> AddTheDesignation(DesignationRequestDto designationDto) {

        DesignationDetails designationDetails = new DesignationDetails();
        designationDetails.setDesignationName(designationDto.getDesignationName());
        designationDetails.setIsActive(true);
        designationDetails.setCreatedDate(LocalDateTime.now());
        designationDetails.setCreatedBy("User");
        designationDetails.setDepartmentId(Long.parseLong(designationDto.getDepartmentId()));
        designationDetails.setTotalEmployee(0L);
        return new ResponseEntity<>(designationRepository.save(designationDetails), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> UpdateTheDesignation(DesignationRequestDto designationDto) {
        Optional<DesignationDetails> desigOptional = getthedesignationdetailsbyid(Long.parseLong(designationDto.getDesignationId()));
        Optional<DepartmentDetails> departOptional = departmentService.getthedepartmentbyid(Long.parseLong(designationDto.getDepartmentId()));

        DesignationDetails designationDetails = desigOptional.get();
        designationDetails.setDepartmentId(Long.parseLong(designationDto.getDepartmentId()));
        designationDetails.setDesignationName(designationDto.getDesignationName());
        designationDetails.setUpdatedBy("Admin");
        designationDetails.setUpdatedDate(LocalDateTime.now());
        designationDetails.setIsActive(designationDto.getIsActive());
        return new ResponseEntity<>(designationRepository.save(designationDetails), HttpStatus.OK);

    }

    public DesignationDetails savethedesignation(DesignationDetails designationDetails) {
        return designationRepository.save(designationDetails);
    }

    public boolean CheckifDesignationisRefferedtoAnyEmployee(Long id) {
        return employeeRepository.existsByDesignationDetails_DesignationId(id);
    }

    public List<DesignationDetails> GetTheListOfDesigantionAsperTheDeptid(Long id) {
        return designationRepository.findByDepartmentId(id);
    }

    public Optional<DesignationDetails> CheckIfDesignatioNmaeExistorNot(String name) {
        return designationRepository.findByDesignationName(name);
    }

    public Boolean CheckIfDuplicateDesignationIsAvaliableForthatDepartment(Long departmentId, String designationName) {
        return designationRepository.existsByDepartmentIdAndDesignationName(departmentId, designationName);
    }
}
