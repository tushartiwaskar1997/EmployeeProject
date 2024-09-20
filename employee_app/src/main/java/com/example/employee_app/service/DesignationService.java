package com.example.employee_app.service;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.DesignationRequestDto;

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

    public List<DesignationDetails> GetTheListOfDesignation() {
        return designationRepository.findAll();
    }

    public Optional<DesignationDetails> GetTheDesignationDetailsById(Long id) {
        return designationRepository.findById(id);
    }

    public String DeleteTheDesignationById(Long id) {
        Optional<DesignationDetails> designationOptional = GetTheDesignationDetailsById(id);
        DesignationDetails designationDetails = designationOptional.get();
        designationDetails.setIsActive(false);
        designationDetails.setUpdatedBy("Admin");
        designationDetails.setUpdatedDate(LocalDateTime.now());
        designationRepository.save(designationDetails);
        return MessageConfig.SUCCESS_DESIGNATION_DELETED;

    }

    public DesignationDetails AddTheDesignation(DesignationRequestDto designationDto) {
        DesignationDetails designationDetails = new DesignationDetails();
        designationDetails.setDesignationName(designationDto.getDesignationName());
        designationDetails.setIsActive(true);
        designationDetails.setCreatedDate(LocalDateTime.now());
        designationDetails.setCreatedBy("User");
        designationDetails.setTotalEmployee(0L);
        return designationRepository.save(designationDetails);
    }

    public DesignationDetails UpdateTheDesignation(DesignationRequestDto designationDto) {
        Optional<DesignationDetails> desigOptional = GetTheDesignationDetailsById((designationDto.getDesignationId()));
        DesignationDetails designationDetails = desigOptional.get();
        designationDetails.setDesignationName(designationDto.getDesignationName());
        designationDetails.setUpdatedBy("Admin");
        designationDetails.setUpdatedDate(LocalDateTime.now());
        designationDetails.setIsActive(true);
        return designationRepository.save(designationDetails);
    }

    public void SaveTheDesignation(DesignationDetails designationDetails) {
        designationRepository.save(designationDetails);
    }

    public Optional<DesignationDetails> CheckIfDesignationNameExistOrNot(String name) {
        return designationRepository.findByDesignationName(name);
    }

    public DesignationDetails UpdateTheDesignationStatusById(DesignationDetails designationDetails) {
        designationDetails.setUpdatedDate(LocalDateTime.now());
        designationDetails.setUpdatedBy("Admin");
        designationDetails.setIsActive(true);
        return designationRepository.save(designationDetails);
    }
}
