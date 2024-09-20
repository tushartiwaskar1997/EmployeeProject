package com.example.employee_app.service;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.DepartmentRequestDto;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.model.EmployeeDetails;
import com.example.employee_app.repository.DepartmentRepository;
import com.example.employee_app.repository.DesignationRepository;
import com.example.employee_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<DepartmentDetails> GetTheDepartmentList() {
        return departmentRepository.findAll();
    }

    public Optional<DepartmentDetails> GetTheDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public String DeleteTheDepartmentById(Long id) {
        Optional<DepartmentDetails> departmentOptional = GetTheDepartmentById(id);
        DepartmentDetails departmentDetails = departmentOptional.get();
        departmentDetails.setIsActive(false);
        departmentDetails.setUpdatedBy("Admin");
        departmentDetails.setUpdatedDate(LocalDateTime.now());
        departmentRepository.save(departmentDetails);
        return MessageConfig.SUCCESS_DEPARTMENT_DELETED;
    }

    public DepartmentDetails AddTheDepartment(DepartmentRequestDto departmentdto) {
        DepartmentDetails dept = new DepartmentDetails();
        dept.setDepartmentName(departmentdto.getDepartmentName());
        dept.setIsActive(true);
        dept.setCreatedDate(LocalDateTime.now());
        dept.setCreatedBy("User");
        dept.setTotalEmployee(0L);
        return departmentRepository.save(dept);
    }

    public DepartmentDetails UpdateTheDepartment(DepartmentRequestDto departmentRequestDto) {
        Optional<DepartmentDetails> deptoptional = GetTheDepartmentById((departmentRequestDto.getDepartmentId()));
        DepartmentDetails dept = deptoptional.get();
        if (!dept.getDepartmentName().equals(departmentRequestDto.getDepartmentName())) {
            Optional<DepartmentDetails> checkifDepartmentpresent = CheckIfDepartmentIsPresent(departmentRequestDto.getDepartmentName().toLowerCase());
            if (!checkifDepartmentpresent.isPresent()) {
                dept.setDepartmentName(departmentRequestDto.getDepartmentName());
            }
        }
        dept.setIsActive(true);
        dept.setUpdatedDate(LocalDateTime.now());
        dept.setUpdatedBy("Admin");
        return departmentRepository.save(dept);
    }

    public DepartmentDetails SaveTheDepartmentDetails(DepartmentDetails dept) {
        return departmentRepository.save(dept);
    }


    public Optional<DepartmentDetails> CheckIfDepartmentIsPresent(String depname) {
        return departmentRepository.findBydepartmentName(depname);
    }

    public Boolean CheckIfAnyDesignationIsStatusTrue(Long id) {
        List<EmployeeDetails> EmpListASPerDeptId = employeeRepository.GetTheListOfEmployeeBYDepartmentId(id);
        if (!EmpListASPerDeptId.isEmpty()) {
            List<DesignationDetails> DesignationList = new ArrayList<>();
            for (EmployeeDetails emp : EmpListASPerDeptId) {
                if (emp.getDesignationDetails().getIsActive()) {
                    return true;
                }
            }
        }
        return false;
    }

    public DepartmentDetails ChangeTheStatusOFDepartmentTOActive(DepartmentDetails departmentDetails) {
        departmentDetails.setUpdatedDate(LocalDateTime.now());
        departmentDetails.setUpdatedBy("Admin");
        departmentDetails.setIsActive(true);
        return departmentRepository.save(departmentDetails);
    }
}
